package com.usco.edu.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.usco.edu.entities.Periodo;

public class PeriodoRowMapper implements RowMapper<Periodo> {
	
	@Override
	public Periodo mapRow(ResultSet rs, int rowNum) throws SQLException {
		Periodo periodo = new Periodo();
		periodo.setCodigo(rs.getInt("per_codigo"));
		periodo.setNombre(rs.getString("per_nombre"));
		periodo.setAnio(rs.getInt("per_año"));
		periodo.setPeriodo(rs.getInt("per_periodo"));
		periodo.setInicio(rs.getDate("per_fecha_inicio"));
		periodo.setFin(rs.getDate("per_fecha_fin"));
		return periodo;
		
	}
}
