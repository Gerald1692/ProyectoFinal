package com.mycompany.proyectofinal;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties; 
import java.io.InputStream;

public class ConexionBD {
    private static final HikariDataSource dataSource;

    static {
        try (InputStream input = ConexionBD.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            
            if (input == null) {
                throw new RuntimeException("No se encontró application.properties");
            }
            
            Properties props = new Properties();
            props.load(input);
            
            HikariConfig config = new HikariConfig();
            
            // Usar SERVICE_NAME si está disponible, de lo contrario usar SID
            String serviceName = props.getProperty("db.service_name");
            String sid = props.getProperty("db.sid");
            
            if (serviceName != null && !serviceName.isEmpty()) {
                config.setJdbcUrl("jdbc:oracle:thin:@//" + 
                    props.getProperty("db.host") + ":" + 
                    props.getProperty("db.port") + "/" + 
                    serviceName);
            } else if (sid != null && !sid.isEmpty()) {
                config.setJdbcUrl("jdbc:oracle:thin:@" + 
                    props.getProperty("db.host") + ":" + 
                    props.getProperty("db.port") + ":" + 
                    sid);
            } else {
                throw new RuntimeException("Debe especificar db.sid o db.service_name");
            }
            
            config.setUsername(props.getProperty("db.user"));
            config.setPassword(props.getProperty("db.password"));
            config.setMaximumPoolSize(10);
            config.setDriverClassName("oracle.jdbc.OracleDriver");
            
            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            throw new RuntimeException("Error al configurar el pool de conexiones", e);
        }
    }

    public static Connection conectar() throws SQLException {
        return dataSource.getConnection();
    }
}