package pe.edu.cibertec.patitas_frontend_wc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.patitas_frontend_wc.client.LogoutClient;
import pe.edu.cibertec.patitas_frontend_wc.dto.LoginResponseDTO;
import pe.edu.cibertec.patitas_frontend_wc.dto.LogoutRequestDTO;
import pe.edu.cibertec.patitas_frontend_wc.dto.LogoutResponseDTO;
import pe.edu.cibertec.patitas_frontend_wc.viewmodel.LoginModel;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/loginfeign")
@CrossOrigin(origins = "http://localhost:5173")
public class LoginFeignClientController {

    @Autowired
    LogoutClient logoutClient;

    @PostMapping("/logout-async")
    public LogoutResponseDTO logout(@RequestBody LogoutRequestDTO logoutRequestDTO) {
        System.out.println("Consumiendo con Feign Client");
        //validar campos de entrada
        if(logoutRequestDTO.TipoDocumento() == null || logoutRequestDTO.TipoDocumento().trim().length()==0
                ||logoutRequestDTO.NumeroDocumento() == null || logoutRequestDTO.NumeroDocumento().trim().length()==0) {
            LoginModel loginModel = new LoginModel("91","Error: Debe completar correctamente sus credenciales","");

            return new LogoutResponseDTO(false,null,"Error: Debe completar correctamente sus credenciales"
            );
        }
        try {
            ResponseEntity<LogoutResponseDTO> responseEntity = logoutClient.logout(logoutRequestDTO);

            if(responseEntity.getStatusCode().is2xxSuccessful()){

                LogoutResponseDTO logoutResponseDTO = responseEntity.getBody()if (logoutResponseDTO.Resultado().equals(true)){
                    //algo
                        return new LogoutResponseDTO(true,logoutResponseDTO.Fecha(),logoutResponseDTO.MensajeError());
                    }else {
                    //otra cosa
                        return new LogoutResponseDTO(false,null,"Error: No se pudo cerrar sesion");
                        }
                    }
            else {
                return new LogoutResponseDTO(false,null,"Error: No se pudo cerrar sesion");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new LogoutResponseDTO(false,null,"Error: Error en el logout");
        }
    }
}
