package com.usco.edu.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.usco.edu.entities.DatosComplementarios;

public class DatosComplementariosRowMapper implements RowMapper<DatosComplementarios> {
	
	@Override
	public DatosComplementarios mapRow(ResultSet rs, int rowNum) throws SQLException {
		DatosComplementarios datosComplementarios = new DatosComplementarios();
		datosComplementarios.setCodigo(rs.getInt("dac_codigo"));
		datosComplementarios.setEstudianteCodigo(rs.getString("est_codigo"));
		datosComplementarios.setModalidad(rs.getString("dac_trabajo_grado"));
		datosComplementarios.setPerfilProfesional(rs.getString("dac_perfil_profesional"));
		datosComplementarios.setPrograma(rs.getString("uaa_nombre"));
		datosComplementarios.setFechaGrado(rs.getDate("gra_fecha"));
		return datosComplementarios;
	}
}
