package dev.siqueira.redis_cache_gaming.controller;

import dev.siqueira.redis_cache_gaming.dtos.*;
import dev.siqueira.redis_cache_gaming.entity.User;
import dev.siqueira.redis_cache_gaming.exception.TooManyRequestsException;
import dev.siqueira.redis_cache_gaming.mapper.UserMapper;
import dev.siqueira.redis_cache_gaming.service.PointsService;
import dev.siqueira.redis_cache_gaming.service.RateLimitService;
import dev.siqueira.redis_cache_gaming.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Usuário e Ranqueamento", description = "São os endpoints responsáveis por gerenciar os usuarios e gerar o ranking")
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PointsService pointsService;
    private final RateLimitService rateLimitService;

    public UserController(UserService userService, PointsService pointsService, RateLimitService rateLimitService) {
        this.userService = userService;
        this.pointsService = pointsService;
        this.rateLimitService = rateLimitService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca de Usuários", description = "Realiza a busca de Usuários no banco, pelo seu ID ")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado!")
    @ApiResponse(responseCode = "200", description = "Usuário encontrado no banco!")
    @ApiResponse(responseCode = "429", description = "Muitos chamados a API do sistema!")
    public ResponseEntity<?> findById(@PathVariable Long id){
        try {
            rateLimitService.rateLimit(id);
            User userDB = userService.findById(id);

            if (userDB == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toUserResponseDto(userDB));
        }catch (TooManyRequestsException e){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(e.getMessage());
        }
    }

    @GetMapping("/ranking/{limit}")
    @Operation(summary = "Ranking", description = "Gera o ranking por pontuação dos usuários!")
    @ApiResponse(responseCode = "404", description = "Sem usuários válidos!")
    @ApiResponse(responseCode = "200", description = "Ranking gerado!")
    public ResponseEntity<List<RankingResponseDto>> getRanking(@PathVariable int limit){
        List<RankingResponseDto> ranking = userService.getRanking(limit);

        if(ranking == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(ranking);
    }

    @PostMapping
    @Operation(summary = "Salvar Usuário", description = "Salva usuários ao banco de dados!")
    @ApiResponse(responseCode = "200", description = "Usuário salvo!")
    public ResponseEntity<User> save(@RequestBody UserRequestDto userRequestDto){
        User userSaved = userService.save(UserMapper.toEntity(userRequestDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(userSaved);
    }

    @PostMapping("/{id}/score")
    @Operation(summary = "Incrementa Pontuação", description = "Adiciona pontos a conta do usuário!")
    @ApiResponse(responseCode = "400", description = "Houve um problema na adição dos pontos!")
    @ApiResponse(responseCode = "200", description = "Pontuação enviada!")
    private ResponseEntity<PointsResponseDto> plusPoints(@PathVariable Long id, @RequestBody PointsRequestDto pointsRequestDto){
        PointsResponseDto pointsResponseDto = pointsService.plusPoints(id, pointsRequestDto);

        if(pointsResponseDto == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(pointsResponseDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza Usuário", description = "muda as informações dos usuários!")
    @ApiResponse(responseCode = "404", description = "Usuário não localizado!")
    @ApiResponse(responseCode = "200", description = "Usuário alterado!")
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto){
        User userUpdated = userService.update(id, userRequestDto);

        if(userUpdated == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toUserResponseDto(userUpdated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta Usuário", description = "Remove o usuário do banco de dados!")
    @ApiResponse(responseCode = "204", description = "Usuário removido!")
    public ResponseEntity<UserResponseDto> delete(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
