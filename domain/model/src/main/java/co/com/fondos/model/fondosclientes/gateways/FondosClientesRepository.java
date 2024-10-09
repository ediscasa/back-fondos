package co.com.fondos.model.fondosclientes.gateways;

import co.com.fondos.model.fondosclientes.FondosClientes;

import java.util.List;
import java.util.Optional;

public interface FondosClientesRepository {

    List<FondosClientes> getEntityBeginsWith(String partitionKey, String sortKey);

    FondosClientes save(FondosClientes model);

    void delete(FondosClientes model);

    Optional<FondosClientes> getLastEntityBeginsWith(String partitionKey, String sortKey);

    Optional<FondosClientes> getEntityByKeys(String partitionKey, String sortKey);

}
