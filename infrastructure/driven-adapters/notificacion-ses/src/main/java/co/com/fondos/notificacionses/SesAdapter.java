package co.com.fondos.notificacionses;

import co.com.fondos.model.fondosclientes.gateways.NotificacionEmailService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
public class SesAdapter implements NotificacionEmailService {

    private final SesClient sesClient;

    public SesAdapter(SesClient sesClient) {
        this.sesClient = sesClient;
    }

    public String enviarEmail(String mensaje, String email) {
        // Configurar el contenido del correo (asunto y cuerpo)
        Content subject = Content.builder()
                .data("Suscripcion a Fondo")
                .build();

        Content textBody = Content.builder()
                .data(mensaje)
                .build();

        Body body = Body.builder()
                .text(textBody)  // También puedes agregar un cuerpo en formato HTML con body.html()
                .build();

        Message message = Message.builder()
                .subject(subject)
                .body(body)
                .build();

        // Configurar la solicitud de envío de correo
        SendEmailRequest emailRequest = SendEmailRequest.builder()
                .destination(Destination.builder().toAddresses(email).build())
                .message(message)
                .source("edissonc@gmail.com")  // Dirección de correo del remitente (verificada en SES)
                .build();

        // Enviar el correo y obtener la respuesta
        SendEmailResponse response = sesClient.sendEmail(emailRequest);

        // Retornar el MessageId de la respuesta
        return response.messageId();
    }
}
