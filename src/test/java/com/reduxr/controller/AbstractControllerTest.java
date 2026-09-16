package com.reduxr.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.mysql.MySQLContainer;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
public abstract class AbstractControllerTest {
    @ServiceConnection
    protected static final MySQLContainer container = new MySQLContainer("mysql");
    
    static {
        container.start();
        Runtime.getRuntime().addShutdownHook(new Thread(container::stop));
    }
    
    protected static MockMvc mockMvc;
    
    protected ObjectMapper objectMapper = new ObjectMapper();
    
    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }
    
    @BeforeEach
    void setUp(@Autowired DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/remove-books-with-categories.sql")
            );
            
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/insert-books-with-categories.sql")
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
