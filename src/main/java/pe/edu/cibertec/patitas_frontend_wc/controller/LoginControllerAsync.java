package pe.edu.cibertec.patitas_frontend_wc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.cibertec.patitas_frontend_wc.dto.LoginRequestDTO;
import pe.edu.cibertec.patitas_frontend_wc.dto.LoginResponseDTO;
import pe.edu.cibertec.patitas_frontend_wc.dto.LogoutRequestDTO;
import pe.edu.cibertec.patitas_frontend_wc.dto.LogoutResponseDTO;
import pe.edu.cibertec.patitas_frontend_wc.viewmodel.LoginModel;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:5173")
public class LoginControllerAsync {

    @Autowired
    WebClient webClientAutenticacion;



    @PostMapping("/autenticar-async")
    public Mono<LoginResponseDTO> autenticar(@RequestBody LoginRequestDTO loginRequestDTO) {
        //validar campos de entrada
        if(loginRequestDTO.TipoDocumento() == null || loginRequestDTO.TipoDocumento().trim().length()==0
                ||loginRequestDTO.NumeroDocumento() == null || loginRequestDTO.NumeroDocumento().trim().length()==0
                || loginRequestDTO.Password() == null || loginRequestDTO.Password().trim().length()==0){
            LoginModel loginModel = new LoginModel("01","Error: Debe completar correctamente sus credenciales","");

            return Mono.just(new LoginResponseDTO("01","Error: Debe completar correctamente sus credenciales","","",
                    "",""));
        }
       try {
           //Invocar Api
           return webClientAutenticacion.post()
                   .uri("/login")
                   .body(Mono.just(loginRequestDTO), LoginResponseDTO.class)
                   .retrieve()
                   .bodyToMono(LoginResponseDTO.class)
                   .flatMap(response ->{
                       if(response.codigo().equals("00")){
                           //algo
                           return Mono.just(new LoginResponseDTO("00","",response.tipoDocumento(), response.numeroDocumento(),response.nombreUsuario(),response.correoUsuario()));
                       }else {
                           //otra cosa
                           return Mono.just(new LoginResponseDTO("02","Error, Autenticacion fallida","","","",""));
                       }
                   });
       } catch (Exception e) {
           System.out.println(e.getMessage());
           return Mono.just(new LoginResponseDTO("99","Error: Error en la autenticacion","","","",""));
       }
    }
    @PostMapping("/logout-async")
    public Mono<LogoutResponseDTO> logout(@RequestBody LogoutRequestDTO logoutRequestDTO) {
        //validar campos de entrada
        if(logoutRequestDTO.TipoDocumento() == null || logoutRequestDTO.TipoDocumento().trim().length()==0
                ||logoutRequestDTO.NumeroDocumento() == null || logoutRequestDTO.NumeroDocumento().trim().length()==0) {
            LoginModel loginModel = new LoginModel("91","Error: Debe completar correctamente sus credenciales","");

            return Mono.just(new LogoutResponseDTO(false,null,"Error: Debe completar correctamente sus credenciales"
                    ));
        }
        try {
            //Invocar Api
            return webClientAutenticacion.post()
                    .uri("/logout")
                    .body(Mono.just(logoutRequestDTO), LogoutResponseDTO.class)
                    .retrieve()
                    .bodyToMono(LogoutResponseDTO.class)
                    .flatMap(response ->{
                        if(response.Resultado().equals(true)){
                            //algo
                            return Mono.just(new LogoutResponseDTO(true,response.Fecha(),response.MensajeError()));
                        }else {
                            //otra cosa
                            return Mono.just(new LogoutResponseDTO(false,null,"Error: No se pudo cerrar sesion"));
                        }
                    });
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Mono.just(new LogoutResponseDTO(false,null,"Error: Error en el logout"));
        }
    }

}
