package co.com.fondos.notificacionsns;

import co.com.fondos.model.fondosclientes.gateways.NotificacionSmsService;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Service
public class SnsAdapter implements NotificacionSmsService {

    private final SnsClient snsClient;

    public SnsAdapter(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    public String enviarSms(String mensaje, String telefono) {
            PublishRequest publishRequest = PublishRequest.builder()
                    .message(mensaje)
                    .phoneNumber(telefono)
                    .build();

            PublishResponse publishResponse = snsClient.publish(publishRequest);
            return publishResponse.messageId();
    }
}
