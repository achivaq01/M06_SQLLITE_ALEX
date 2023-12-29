package com.project.Interfaces;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author achica
 */
public interface Print {
    void execute(String query, Connection conn, PrintHeader action, String title) throws SQLException;
    
}
