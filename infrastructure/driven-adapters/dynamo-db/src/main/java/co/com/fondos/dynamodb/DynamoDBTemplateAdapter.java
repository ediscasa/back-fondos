package co.com.fondos.dynamodb;

import co.com.fondos.dynamodb.helper.TemplateAdapterOperations;
import co.com.fondos.model.fondosclientes.FondosClientes;
import co.com.fondos.model.fondosclientes.gateways.FondosClientesRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.List;
import java.util.Optional;

@Repository
public class DynamoDBTemplateAdapter extends TemplateAdapterOperations<FondosClientes, String, FondosClientesEntity> implements FondosClientesRepository {

    public DynamoDBTemplateAdapter(DynamoDbEnhancedClient connectionFactory, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(connectionFactory, mapper, d -> mapper.map(d, FondosClientes.class), "fondos_clientes");
    }

    public List<FondosClientes> getEntityBeginsWith(String partitionKey, String sortKey) {
        QueryEnhancedRequest queryExpression = generateQueryExpressionBeginsWith(partitionKey, sortKey);
        return query(queryExpression);
    }

    private QueryEnhancedRequest generateQueryExpressionBeginsWith(String partitionKey, String sortKey) {
        return QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.sortBeginsWith(Key.builder().partitionValue(partitionKey).sortValue(sortKey).build()))
                .build();
    }

    public Optional<FondosClientes> getLastEntityBeginsWith(String partitionKey, String sortKey) {
        QueryEnhancedRequest queryExpression = generateQueryExpressionBeginsWith(partitionKey, sortKey).toBuilder()
                .limit(1)
                .scanIndexForward(false)
                .build();
        return query(queryExpression).stream().findFirst();
    }

    private QueryEnhancedRequest generateQueryExpressionEqualTo(String partitionKey, String sortKey) {
        return QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(Key.builder().partitionValue(partitionKey).sortValue(sortKey).build()))
                .build();
    }

    public Optional<FondosClientes> getEntityByKeys(String partitionKey, String sortKey) {
        QueryEnhancedRequest queryExpression = generateQueryExpressionEqualTo(partitionKey, sortKey).toBuilder()
                .build();
        return query(queryExpression).stream().findFirst();
    }



}
