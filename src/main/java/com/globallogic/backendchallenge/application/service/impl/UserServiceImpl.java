package com.globallogic.backendchallenge.application.service.impl;

import com.globallogic.backendchallenge.application.service.UserService;
import com.globallogic.backendchallenge.domain.dto.CreateUserRequestDto;
import com.globallogic.backendchallenge.domain.dto.CreateUserResponseDto;
import com.globallogic.backendchallenge.domain.dto.GetUserResponseDto;
import com.globallogic.backendchallenge.domain.exception.UnableToCreateUserException;
import com.globallogic.backendchallenge.domain.exception.UnableToRetrieveUserException;
import com.globallogic.backendchallenge.domain.exception.RecordAlreadyExistsException;
import com.globallogic.backendchallenge.domain.validation.ValidateInputParams;
import com.globallogic.backendchallenge.infrastructure.adapters.output.persistence.entity.PhoneId;
import com.globallogic.backendchallenge.infrastructure.adapters.output.persistence.entity.UserEntity;
import com.globallogic.backendchallenge.infrastructure.adapters.output.persistence.mapper.UserMapper;
import com.globallogic.backendchallenge.infrastructure.adapters.output.persistence.repository.PhoneRepository;
import com.globallogic.backendchallenge.infrastructure.adapters.output.persistence.repository.UserRepository;
import com.globallogic.backendchallenge.infrastructure.security.DataEncryptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;
    private final UserMapper mapper;
    private final UserTokenService tokenService;
    private final DataEncryptionService encryptionService;

    @Override
    public GetUserResponseDto getUserByToken(String token) {
        try {
            Map<String, Object> tokenPayload = tokenService.parseToken(token.substring(7));

            return Optional.ofNullable(tokenPayload.get("uuid"))
                    .map(String.class::cast)
                    .flatMap(tokenUuid -> userRepository.findByUserUuid(tokenUuid)
                            .map(entity -> {
                                GetUserResponseDto response = mapper.toGetResponseDto(entity);
                                response.setId(tokenUuid);
                                response.setPassword(encryptionService.decrypt(entity.getPassword()));
                                response.setToken(tokenService.generateToken(tokenUuid));
                                return response;
                            }))
                    .orElseThrow(() -> new UnableToRetrieveUserException("Error retrieving user"));
        } catch (PersistenceException exception) {
            log.error("User could not be retrieved from the database");
            throw new UnableToRetrieveUserException("Error retrieving user");
        }
    }

    @Override
    @ValidateInputParams
    public CreateUserResponseDto createUser(CreateUserRequestDto request) {
        try {
            checkExistingRecords(request);
            UserEntity userEntity = mapToEntity(request);
            userEntity.setPassword(encryptionService.encrypt(request.getPassword()));

            return Optional.of(userRepository.save(userEntity))
                    .map(entity -> {
                        CreateUserResponseDto responseDto = mapper.toCreateResponseDto(userEntity);
                        responseDto.setToken(tokenService.generateToken(entity.getUserUuid()));
                        return responseDto;
                    })
                    .orElseThrow(() -> new UnableToCreateUserException("Failed to create user"));
        } catch (PersistenceException exception) {
            log.error(String.format("Failed to create user - Exception message: %s",
                    exception.getMessage()));
            throw new UnableToCreateUserException(String.format("An error occurred while creating user [%s]",
                    request.getName()));
        }
    }

    private void checkExistingRecords(CreateUserRequestDto request) {
        if (userRepository.existsByNameAndEmail(request.getName(), request.getEmail())) {
            throw new RecordAlreadyExistsException(String.format("There is already an existing user with name: " +
                    "%s, and email: %s", request.getName(), request.getEmail()));
        }

        if (CollectionUtils.isNotEmpty(request.getPhones())) {
            request.getPhones().forEach(phone -> {
                PhoneId id = PhoneId.builder().number(phone.getNumber()).cityCode(phone.getCityCode())
                        .countryCode(phone.getCountryCode()).build();
                if (phoneRepository.existsById(id)) {
                    throw new RecordAlreadyExistsException(String.format("There is already an existing phone with " +
                            "number: %s, city code: %s and country code: %s", phone.getNumber(), phone.getCityCode(),
                            phone.getCountryCode()));
                }
            });
        }
    }

    private UserEntity mapToEntity(CreateUserRequestDto request) {
        UserEntity userEntity = mapper.toEntity(request);
        if (CollectionUtils.isNotEmpty(userEntity.getPhones())) {
            userEntity.getPhones().forEach(phone -> phone.setUser(userEntity));
        }

        return userEntity;
    }
}
