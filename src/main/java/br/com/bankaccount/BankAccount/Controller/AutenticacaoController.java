package br.com.bankaccount.BankAccount.Controller;

import br.com.bankaccount.BankAccount.dto.AutenticacaoDto.DadosLoginDto;
import br.com.bankaccount.BankAccount.dto.AutenticacaoDto.TokenJwtDto;
import br.com.bankaccount.BankAccount.model.User;
import br.com.bankaccount.BankAccount.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
@Tag(name = "Entrar", description = "Autenticação de Login de usuário")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    @Operation(summary = "Login de Usuário", description = "Login de Usuário através de Login (cpf ou cnpj) e Senha," +
            " validando o Token caso esteja correto.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token gerado com sucesso!",
            content = @Content(schema = @Schema(implementation = TokenJwtDto.class), mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Dados inválidos ou má formatação json!"),
            @ApiResponse(responseCode = "500", description = "Erro de servidor.")
    }
    )
    public ResponseEntity<TokenJwtDto> login(@RequestBody @Valid DadosLoginDto loginDto){
        var autenticacaoToken = new UsernamePasswordAuthenticationToken(loginDto.cpfCnpj(), loginDto.password());
        var authenticate = authenticationManager.authenticate(autenticacaoToken);
        var token = tokenService.gerarToken((User) authenticate.getPrincipal());
        return ResponseEntity.ok(new TokenJwtDto(token));
    }

}
