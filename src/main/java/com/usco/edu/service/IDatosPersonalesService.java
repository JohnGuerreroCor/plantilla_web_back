package com.usco.edu.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.usco.edu.entities.DatosPersonales;
import com.usco.edu.entities.EstadoCivil;
import com.usco.edu.entities.GrupoSanguineo;
import com.usco.edu.entities.Respuesta;
import com.usco.edu.entities.SoporteExpedicion;
import com.usco.edu.entities.TipoDocumento;

public interface IDatosPersonalesService {
	
	public List<DatosPersonales> obtenerDatosPersonales(int perCodigo);
	
	public List<TipoDocumento> obtenerIdentificacionTipos();
	
	public List<EstadoCivil> obtenerEstadosCivil();
	
	public List<GrupoSanguineo> obtenerGruposSanguineos();
	
	public int actualizarDatosContacto(DatosPersonales contacto);
	
	public int actualizarDatosResidencia(DatosPersonales residencia);
	
	public int actualizarDatosExpedicion(String userdb, DatosPersonales expedicion);
	
	public void registrarSoporteExpedicion(String userdb, SoporteExpedicion soporte);
	
	public void actualizarSoporteExpedicion(String userdb, SoporteExpedicion soporte);
	
	String subirSoporteExpedicion(MultipartFile file, Long perCodigo, int uaa, String userdb, HttpServletRequest request);
	
	Respuesta enviarEmailCambioExpedicion(String email);

	ByteArrayInputStream mirarSoporteExpedicion(long archivoCodigo, String userdb, HttpServletResponse response);

}
