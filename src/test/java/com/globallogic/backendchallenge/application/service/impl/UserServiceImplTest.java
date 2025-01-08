package com.globallogic.backendchallenge.application.service.impl;

import com.globallogic.backendchallenge.domain.dto.CreateUserRequestDto;
import com.globallogic.backendchallenge.domain.dto.CreateUserResponseDto;
import com.globallogic.backendchallenge.domain.dto.GetUserResponseDto;
import com.globallogic.backendchallenge.domain.dto.PhoneDto;
import com.globallogic.backendchallenge.domain.exception.RecordAlreadyExistsException;
import com.globallogic.backendchallenge.domain.exception.UnableToCreateUserException;
import com.globallogic.backendchallenge.domain.exception.UnableToRetrieveUserException;
import com.globallogic.backendchallenge.infrastructure.adapters.output.persistence.entity.PhoneId;
import com.globallogic.backendchallenge.infrastructure.adapters.output.persistence.entity.UserEntity;
import com.globallogic.backendchallenge.infrastructure.adapters.output.persistence.mapper.UserMapper;
import com.globallogic.backendchallenge.infrastructure.adapters.output.persistence.repository.PhoneRepository;
import com.globallogic.backendchallenge.infrastructure.adapters.output.persistence.repository.UserRepository;
import com.globallogic.backendchallenge.infrastructure.security.DataEncryptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.PersistenceException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class})
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PhoneRepository phoneRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserTokenService tokenService;

    @Mock
    private DataEncryptionService encryptionService;

    @Test
    void getUserByTokenValidTokenSuccess() {
        String token = "Bearer validToken";
        String tokenUuid = "test-uuid";

        Map<String, Object> tokenPayload = Map.of("uuid", tokenUuid);
        UserEntity userEntity = UserEntity.builder()
                .password("encryptedPassword")
                .build();
        GetUserResponseDto responseDto = GetUserResponseDto.builder()
                .id(tokenUuid)
                .build();

        Mockito.when(tokenService.parseToken("validToken")).thenReturn(tokenPayload);
        Mockito.when(userRepository.findByUserUuid(tokenUuid)).thenReturn(Optional.of(userEntity));
        Mockito.when(userMapper.toGetResponseDto(userEntity)).thenReturn(responseDto);
        Mockito.when(encryptionService.decrypt("encryptedPassword")).thenReturn("decryptedPassword");
        Mockito.when(tokenService.generateToken(tokenUuid)).thenReturn("newToken");

        GetUserResponseDto result = userService.getUserByToken(token);

        assertNotNull(result);
        assertEquals(tokenUuid, result.getId());
        assertEquals("decryptedPassword", result.getPassword());
        assertEquals("newToken", result.getToken());

        Mockito.verify(tokenService).parseToken("validToken");
        Mockito.verify(userRepository).findByUserUuid(tokenUuid);
    }

    @Test
    void getUserByTokenWithUnableToRetrieveUserException() {
        String token = "Bearer validToken";
        String tokenUuid = "test-uuid";

        Map<String, Object> tokenPayload = Map.of("uuid", tokenUuid);

        Mockito.when(tokenService.parseToken("validToken")).thenReturn(tokenPayload);
        Mockito.when(userRepository.findByUserUuid(tokenUuid)).thenReturn(Optional.empty());

        assertThrows(UnableToRetrieveUserException.class, () -> userService.getUserByToken(token));

        Mockito.verify(tokenService).parseToken("validToken");
        Mockito.verify(userRepository).findByUserUuid(tokenUuid);
    }

    @Test
    void createUserSuccessValidRequest() {
        CreateUserRequestDto request = getCreateRequestDto();
        UserEntity userEntity = UserEntity.builder()
                .userUuid("userUuid")
                .build();
        CreateUserResponseDto responseDto = CreateUserResponseDto.builder().build();

        Mockito.when(userRepository.existsByNameAndEmail(request.getName(), request.getEmail())).thenReturn(false);
        Mockito.when(userMapper.toEntity(request)).thenReturn(userEntity);
        Mockito.when(encryptionService.encrypt("password")).thenReturn("encryptedPassword");
        Mockito.when(userRepository.save(userEntity)).thenReturn(userEntity);
        Mockito.when(userMapper.toCreateResponseDto(userEntity)).thenReturn(responseDto);
        Mockito.when(tokenService.generateToken("userUuid")).thenReturn("newToken");

        CreateUserResponseDto result = userService.createUser(request);

        assertNotNull(result);
        Mockito.verify(userRepository).save(userEntity);
        Mockito.verify(tokenService).generateToken("userUuid");
    }

    @Test
    void createUserWithRecordAlreadyExistsException() {
        CreateUserRequestDto request = getCreateRequestDto();

        Mockito.when(userRepository.existsByNameAndEmail(request.getName(), request.getEmail())).thenReturn(true);
        assertThrows(RecordAlreadyExistsException.class, () -> userService.createUser(request));
        Mockito.verify(userRepository).existsByNameAndEmail(request.getName(), request.getEmail());
    }

    @Test
    void createUserPhoneRecordAlreadyExistsException() {
        CreateUserRequestDto request = getCreateRequestDto();
        PhoneDto phoneDto = PhoneDto.builder()
                .number(123456L)
                .countryCode("01")
                .cityCode(11)
                .build();
        request.setPhones(Collections.singletonList(phoneDto));

        PhoneId phoneId = PhoneId.builder().number(phoneDto.getNumber()).cityCode(phoneDto.getCityCode())
                .countryCode(phoneDto.getCountryCode()).build();

        Mockito.when(userRepository.existsByNameAndEmail(request.getName(), request.getEmail())).thenReturn(false);
        Mockito.when(phoneRepository.existsById(phoneId)).thenReturn(true);
        assertThrows(RecordAlreadyExistsException.class, () -> userService.createUser(request));
        Mockito.verify(phoneRepository).existsById(phoneId);
    }

    private CreateUserRequestDto getCreateRequestDto() {
        return CreateUserRequestDto.builder()
                .name("Juan")
                .email("juan@email.com")
                .password("password")
                .build();
    }

    @Test
    void createUserWithUnableToCreateUserExceptionWhenPersistenceExceptionOccurs() {
        CreateUserRequestDto request = getCreateRequestDto();

        Mockito.when(userRepository.existsByNameAndEmail(request.getName(), request.getEmail())).thenReturn(false);
        Mockito.when(userMapper.toEntity(request)).thenThrow(new PersistenceException());
        assertThrows(UnableToCreateUserException.class, () -> userService.createUser(request));
        Mockito.verify(userMapper).toEntity(request);
    }

    @Test
    void getUserWithUnableToRetrieveUserExceptionWhenPersistenceExceptionOccurs() {
        String token = "Bearer validToken";
        String tokenUuid = "test-uuid";

        Map<String, Object> tokenPayload = Map.of("uuid", tokenUuid);

        Mockito.when(tokenService.parseToken("validToken")).thenReturn(tokenPayload);
        Mockito.when(userRepository.findByUserUuid(tokenUuid)).thenThrow(new PersistenceException());

        assertThrows(UnableToRetrieveUserException.class, () -> userService.getUserByToken(token));

        Mockito.verify(tokenService).parseToken("validToken");
        Mockito.verify(userRepository).findByUserUuid(tokenUuid);
    }
}