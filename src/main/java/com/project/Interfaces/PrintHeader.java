/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.project.Interfaces;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author achica
 */
public interface PrintHeader {
    void execute(String title, int columnCount) throws SQLException;
}
