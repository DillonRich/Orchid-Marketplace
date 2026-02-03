package com.orchid.orchid_marketplace;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.models.CosmosContainerProperties;
import com.azure.cosmos.models.CosmosContainerResponse;
import com.azure.cosmos.models.CosmosDatabaseResponse;
import com.azure.cosmos.models.CosmosItemRequestOptions;
import com.azure.cosmos.models.CosmosItemResponse;
import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.models.PartitionKey;

/**
 * Standalone test to verify Azure Cosmos DB connection
 * Run: mvn test -Dtest=CosmosConnectionTest
 */
public class CosmosConnectionTest {

    @Test
    public void testCosmosConnection() {
        System.out.println("🚀 Testing Cosmos DB connection...");
        
        // Connection details - Replace with your actual credentials
        String endpoint = "https://your-cosmos-db-account.documents.azure.com:443/";
        String key = "your-cosmos-db-primary-key-here";
        String databaseName = "OrchidMarketplace";
        String containerName = "Users";
        
        CosmosClient client = null;
        try {
            // Create Cosmos client
            System.out.println("📡 Connecting to Cosmos DB...");
            client = new CosmosClientBuilder()
                .endpoint(endpoint)
                .key(key)
                .directMode()
                .buildClient();
            
            // Get or create database
            System.out.println("📦 Getting database: " + databaseName);
            CosmosDatabaseResponse databaseResponse = client.createDatabaseIfNotExists(databaseName);
            CosmosDatabase database = client.getDatabase(databaseName);
            System.out.println("✅ Database exists: " + database.getId());
            
            // Get or create container
            System.out.println("📦 Getting container: " + containerName);
            CosmosContainerProperties containerProperties = new CosmosContainerProperties(containerName, "/email");
            CosmosContainerResponse containerResponse = database.createContainerIfNotExists(containerProperties);
            CosmosContainer container = database.getContainer(containerName);
            System.out.println("✅ Container exists: " + container.getId());
            
            // Create test user
            String testId = UUID.randomUUID().toString();
            String testEmail = "cosmos-test-" + System.currentTimeMillis() + "@orchid.com";
            
            Map<String, Object> testUser = new HashMap<>();
            testUser.put("id", testId);
            testUser.put("email", testEmail);
            testUser.put("fullName", "Cosmos Test User");
            testUser.put("role", "CUSTOMER");
            testUser.put("isActive", true);
            testUser.put("createdAt", new Date().toString());
            
            System.out.println("💾 Creating test user: " + testEmail);
            CosmosItemResponse<Map> response = container.createItem(testUser);
            System.out.println("✅ User created successfully!");
            System.out.println("   Status Code: " + response.getStatusCode());
            System.out.println("   Request Charge: " + response.getRequestCharge() + " RUs");
            System.out.println("   User ID: " + testId);
            System.out.println("   Email: " + testEmail);
            
            // Read back the user
            System.out.println("📖 Reading user back from Cosmos DB...");
            CosmosItemResponse<Map> readResponse = container.readItem(
                testId,
                new PartitionKey(testEmail),
                Map.class
            );
            Map<String, Object> retrievedUser = readResponse.getItem();
            System.out.println("✅ User retrieved successfully!");
            System.out.println("   Full Name: " + retrievedUser.get("fullName"));
            System.out.println("   Role: " + retrievedUser.get("role"));
            
            // Query for the user
            System.out.println("🔍 Querying for user by email...");
            String query = "SELECT * FROM c WHERE c.email = '" + testEmail + "'";
            Iterable<Map> queryResults = container.queryItems(query, new CosmosQueryRequestOptions(), Map.class);
            int count = 0;
            for (Map user : queryResults) {
                count++;
                System.out.println("   Found user: " + user.get("fullName"));
            }
            System.out.println("✅ Query returned " + count + " result(s)");
            
            // Cleanup - delete test user
            System.out.println("🧹 Cleaning up test user...");
            container.deleteItem(testId, new PartitionKey(testEmail), new CosmosItemRequestOptions());
            System.out.println("✅ Test user deleted");
            
            System.out.println("\n🎉 SUCCESS! Azure Cosmos DB is fully functional!");
            System.out.println("\n📋 Next Steps:");
            System.out.println("   1. Go to Azure Portal: https://portal.azure.com");
            System.out.println("   2. Navigate to: Cosmos DB Accounts → orchid-marketplace-db");
            System.out.println("   3. Click: Data Explorer → OrchidMarketplace → Users");
            System.out.println("   4. Register a real user via the frontend to see persistent data");
            
        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Cosmos DB connection test failed", e);
        } finally {
            if (client != null) {
                client.close();
                System.out.println("🔌 Cosmos client closed");
            }
        }
    }
}
