package com.client;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.model.Funcionalidad;
import com.model.Rol;
import com.model.Usuario;
import com.service.FuncionalidadBeanRemote;
import com.service.IUsuarioService;
import com.service.RolBeanRemote;

import java.util.HashSet;
import java.util.Set;

public class Main {

	@EJB
	private static IUsuarioService usuarioService;
	@EJB
	private static RolBeanRemote rolBeanRemote;
	@EJB
	private static FuncionalidadBeanRemote funcionalidadBeanRemote;

	
	public static void main(String[] args) {
		try {
			InitialContext initialContext = new InitialContext();
			usuarioService = (IUsuarioService) initialContext.lookup("ejb:/GestionUsuarios/UsuarioService!com.service.IUsuarioService");
			rolBeanRemote = (RolBeanRemote) initialContext.lookup("ejb:/GestionUsuarios/RolBean!com.service.RolBeanRemote");
			funcionalidadBeanRemote = (FuncionalidadBeanRemote) initialContext.lookup("ejb:/GestionUsuarios/FuncionalidadBean!com.service.FuncionalidadBeanRemote");

//			createRolConFuncionalidadExistente();

			createUsuarioConRolExistente();


		} catch (NamingException e) {
			System.out.println("asdasdasdasdasdasdasd");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("TEST TEST TEST: " + e.getMessage());
			e.printStackTrace();
		}

		System.out.println("test final metodo main");
	}

	public static void createRolConFuncionalidadExistente() {
		try {
			Rol rol = new Rol(null, "Funcionalidad Existente", "Funcionalidad Existente");
			Set<Funcionalidad> funcionalidades = new HashSet<>();
			funcionalidades.add(funcionalidadBeanRemote.obtener(1L));
			funcionalidades.add(funcionalidadBeanRemote.obtener(2L));
			rol.setFuncionalidades(funcionalidades);
			rolBeanRemote.crear(rol);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUsuarioConRolExistente() {
		try {
			Rol rol = rolBeanRemote.obtener(1L);
			System.out.println(rol);
			Usuario usuario = new Usuario(null, "Hernan test", "Testbg", "123123", "asdasdasd", "asdasd@asdasd.com", rol);
			usuarioService.crear(usuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createUsuarioConNuevoRol() {
		try {
			Rol rol = new Rol(null, "Rol con usuario", "Rol con usuario descr");
			Set<Funcionalidad> funcionalidades = new HashSet<>();
			funcionalidades.add(funcionalidadBeanRemote.obtener(1L));
			rol.setFuncionalidades(funcionalidades);
			Usuario usuario = new Usuario(null, "Hernan segundo", "Segundo", "65767867", "bmnnbmnm", "nmbnmbnm@asdasd.com", rol);

			usuarioService.crear(usuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
