package com.usco.edu.dao.daoImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.usco.edu.dao.IDatosPersonalesDao;
import com.usco.edu.entities.DatosPersonales;
import com.usco.edu.entities.EstadoCivil;
import com.usco.edu.entities.GrupoSanguineo;
import com.usco.edu.entities.SoporteExpedicion;
import com.usco.edu.entities.TipoDocumento;
import com.usco.edu.resultSetExtractor.DatosPersonalesSetExtractor;
import com.usco.edu.resultSetExtractor.EstadoCivilSetExtractor;
import com.usco.edu.resultSetExtractor.GrupoSanguineoSetExtractor;
import com.usco.edu.resultSetExtractor.TipoDocumentoSetExtractor;
import com.usco.edu.util.AuditoriaJdbcTemplate;

@Repository
public class DatosPersonalesDaoImpl implements IDatosPersonalesDao {
	
	@Autowired
	private AuditoriaJdbcTemplate jdbcComponent;
	
	@Autowired
	@Qualifier("JDBCTemplatePlanesConsulta")
	public JdbcTemplate jdbcTemplate;
	
	@Autowired
	@Qualifier("JDBCTemplateEjecucion")
	public JdbcTemplate jdbcTemplateEjecucion;

	@Override
	public List<DatosPersonales> obtenerDatosPersonales(int id) {
		
		String sql = "select top 1 p.per_codigo, p.tii_codigo, ti.tii_nombre, p.per_identificacion, p.per_apellido, p.per_nombre, "
				+ "p.per_genero, p.per_estado_civil, ec.esc_nombre, p.grs_codigo, gs.grs_nombre, p.per_fecha_nacimiento, "
				+ "p1.pai_codigo as pai_nacimiento_codigo, p1.pai_nombre as pai_nacimiento, d1.dep_codigo as dep_nacimiento_codigo, d1.dep_nombre as dep_nacimiento, p.per_lugar_nacimiento as mun_nacimiento_codigo, m1.mun_nombre as mun_nacimiento, "
				+ "p2.pai_codigo as pai_expedicion_codigo, p2.pai_nombre as pai_expedicion, d2.dep_codigo as dep_expedicion_codigo, d2.dep_nombre as dep_expedicion, p.per_lugar_expedicion as mun_expedicion_codigo, m2.mun_nombre as mun_expedicion, "
				+ "p.per_fecha_expedicion, p.per_email, p.per_pagina_personal, p.per_telefono_fijo, p.per_telefono_movil, "
				+ "p.per_pais_residencia as pai_residencia_codigo, p3.pai_nombre as pai_residencia, p.per_departamento_residencia as dep_residencia_codigo, d3.dep_nombre as dep_residencia, p.per_lugar_residencia as mun_residencia_codigo, m3.mun_nombre as mun_residencia, "
				+ "p.per_barrio, p.per_direccion_residencia, sfe.sfe_ruta_archivo as sopore_expedicion "
				+ "from persona p "
				+ "left join tipo_id ti on p.tii_codigo = ti.tii_codigo "
				+ "left join estado_civil ec on p.per_estado_civil = ec.esc_codigo "
				+ "left join grupo_sanguineo gs on p.grs_codigo = gs.grs_codigo "
				+ "left join municipio m1 on p.per_lugar_nacimiento = m1.mun_codigo "
				+ "left join municipio m2 on p.per_lugar_expedicion = m2.mun_codigo "
				+ "left join municipio m3 on p.per_lugar_residencia = m3.mun_codigo "
				+ "left join departamento d1 on m1.dep_codigo = d1.dep_codigo "
				+ "left join departamento d2 on m2.dep_codigo = d2.dep_codigo "
				+ "left join departamento d3 on m3.dep_codigo = d3.dep_codigo "
				+ "left join pais p1 on d1.pai_codigo = p1.pai_codigo "
				+ "left join pais p2 on d2.pai_codigo = p2.pai_codigo "
				+ "left join pais p3 on d3.pai_codigo = p3.pai_codigo "
				+ "left join graduado.soporte_fecha_expedicion sfe on p.per_codigo = sfe.per_codigo "
				+ "where p.per_identificacion = '" + id +  "' order by sfe.sfe_codigo desc";
		return jdbcTemplate.query(sql, new DatosPersonalesSetExtractor());
		
	}

	@Override
	public List<TipoDocumento> obtenerIdentificacionTipos() {
		
		String sql = "select * from dbo.tipo_id ti where ti.tii_estado = 1 order by ti.tii_num_caracteres desc";
		return jdbcTemplate.query(sql, new TipoDocumentoSetExtractor());
		
	}

	@Override
	public List<EstadoCivil> obtenerEstadosCivil() {
		
		String sql = "select * from estado_civil ec where SNIES_codigo is not null";
		return jdbcTemplate.query(sql, new EstadoCivilSetExtractor());
		
	}

	@Override
	public List<GrupoSanguineo> obtenerGruposSanguineos() {
		
		String sql = "select * from grupo_sanguineo gs where gs.grs_estado = 1";
		return jdbcTemplate.query(sql, new GrupoSanguineoSetExtractor());
		
	}

	@Override
	public int actualizarDatosContacto(DatosPersonales contacto) {

		String sql = "UPDATE dbo.persona "
				+ "SET per_telefono_movil=?, per_telefono_fijo=?, per_email=?, per_pagina_personal=? "
				+ "WHERE per_codigo=?;";

		int result = jdbcTemplateEjecucion.update(sql, new Object[] {
				contacto.getTelefonoMovil(),
				contacto.getTelefonoFijo(),
				contacto.getEmailPersonal(),
				contacto.getPaginaWeb(),
				contacto.getCodigo()
				});
		
		try {

			MapSqlParameterSource parameter = new MapSqlParameterSource();
			
			parameter.addValue("telefonoMovil", contacto.getTelefonoMovil());
			parameter.addValue("telefonoFijo", contacto.getTelefonoFijo());
			parameter.addValue("emailPersonal", contacto.getEmailPersonal());
			parameter.addValue("paginaWeb", contacto.getPaginaWeb());
			parameter.addValue("personaCodigo", contacto.getCodigo());
			
			return result;

		} catch (Exception e) {

			e.printStackTrace();
			return 0;
		}
		
	}
	

	@Override
	public int actualizarDatosResidencia(DatosPersonales residencia) {

		String sql = "UPDATE dbo.persona "
				+ "SET per_direccion_residencia=?, per_lugar_residencia=?, per_departamento_residencia=?, per_pais_residencia=?, per_barrio=? "
				+ "WHERE per_codigo= ?;";

		int result = jdbcTemplateEjecucion.update(sql, new Object[] {
				residencia.getDireccion(),
				residencia.getMunicipioResidenciaCodigo(),
				residencia.getDepartamentoResidenciaCodigo(),
				residencia.getPaisResidenciaCodigo(),
				residencia.getBarrio(),
				residencia.getCodigo()
				});
		
		try {

			MapSqlParameterSource parameter = new MapSqlParameterSource();
			
			parameter.addValue("direccion", residencia.getDireccion());
			parameter.addValue("municipio", residencia.getMunicipioResidenciaCodigo());
			parameter.addValue("departamento", residencia.getDepartamentoResidenciaCodigo());
			parameter.addValue("pais", residencia.getPaisResidenciaCodigo());
			parameter.addValue("barrio", residencia.getBarrio());
			parameter.addValue("personaCodigo", residencia.getCodigo());
			
			return result;

		} catch (Exception e) {

			e.printStackTrace();
			return 0;
		}
		
	}
	
	@Override
	public int actualizarDatosExpedicion(String userdb, DatosPersonales expedicion) {
		
		DataSource dataSource = jdbcComponent.construirDataSourceDeUsuario(userdb);
		NamedParameterJdbcTemplate jdbc = jdbcComponent.construirTemplatenew(dataSource);

		String sql = "UPDATE dbo.persona "
				+ "SET per_lugar_expedicion=:municipio, per_fecha_expedicion=:fechaExpedicion "
				+ "WHERE per_codigo=:codigo;";

		try {

			MapSqlParameterSource parameter = new MapSqlParameterSource();
			
			parameter.addValue("codigo", expedicion.getCodigo());
			parameter.addValue("municipio", expedicion.getMunicipioExpedicionCodigo());
			parameter.addValue("fechaExpedicion", expedicion.getIdentificacionFechaExpedicion());

			return jdbc.update(sql, parameter);
		} catch (Exception e) {

			e.printStackTrace();
			return 0;
		} finally {
			try {
				cerrarConexion(dataSource.getConnection());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void registrarSoporteExpedicion(String userdb, SoporteExpedicion soporte) {
		
		DataSource dataSource = jdbcComponent.construirDataSourceDeUsuario(userdb);
		NamedParameterJdbcTemplate jdbc = jdbcComponent.construirTemplatenew(dataSource);

		String sql = "INSERT INTO graduado.soporte_fecha_expedicion "
				+ "(per_codigo, sfe_nombre, sfe_ruta_archivo) "
				+ "VALUES(:perCodigo, :nombre, :url);";
		try {

			MapSqlParameterSource parameter = new MapSqlParameterSource();
			
			parameter.addValue("perCodigo", soporte.getPerCodigo());
			parameter.addValue("nombre", soporte.getNombre());
			parameter.addValue("url", soporte.getRuta());

			jdbc.update(sql, parameter);
			System.out.println("PASA POR CREATE");

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			try {
				cerrarConexion(dataSource.getConnection());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void actualizarSoporteExpedicion(String userdb, SoporteExpedicion soporte) {
		
		DataSource dataSource = jdbcComponent.construirDataSourceDeUsuario(userdb);
		NamedParameterJdbcTemplate jdbc = jdbcComponent.construirTemplatenew(dataSource);

		String sql = "UPDATE graduado.soporte_fecha_expedicion "
				+ "SET sfe_nombre=:nombre, sfe_ruta_archivo=:url"
				+ "WHERE sfe_codigo =:codigo;";

		try {

			MapSqlParameterSource parameter = new MapSqlParameterSource();
			
			parameter.addValue("codigo", soporte.getCodigo());
			parameter.addValue("nombre", soporte.getNombre());
			parameter.addValue("url", soporte.getRuta());

			jdbc.update(sql, parameter);
			System.out.println("PASA POR ACTULIZAR");
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			try {
				cerrarConexion(dataSource.getConnection());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void cerrarConexion(Connection con) {
		if (con == null)
			return;

		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
