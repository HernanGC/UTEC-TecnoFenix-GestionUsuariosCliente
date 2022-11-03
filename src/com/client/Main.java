package com.client;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.model.Funcionalidad;
import com.model.Rol;
import com.service.IUsuarioService;
import com.service.RolBeanRemote;

import java.util.HashSet;
import java.util.Set;

public class Main {

	@EJB
	private static IUsuarioService usuarioService;
	@EJB
	private static RolBeanRemote rolBeanRemote;

	
	public static void main(String[] args) {
		try {
			System.out.println("ACA SI");
			InitialContext initialContext = new InitialContext();
			usuarioService = (IUsuarioService) initialContext.lookup("ejb:/GestionUsuarios/UsuarioService!com.service.IUsuarioService");
			rolBeanRemote = (RolBeanRemote) initialContext.lookup("ejb:/GestionUsuarios/RolBean!com.service.RolBeanRemote");

			Rol rol = new Rol(null, "Analista", "Descr 1");
			Rol rol2 = new Rol(null, "Estudiante", "Descr 2");
//
			Set<Funcionalidad> funcionalidades = new HashSet<Funcionalidad>();
			funcionalidades.add(new Funcionalidad(null, "func1", "func-desc1"));
			funcionalidades.add(new Funcionalidad(null, "func2", "func-desc2"));
			
			rol.setFuncionalidades(funcionalidades);
			
			Set<Funcionalidad> funcionalidades2 = new HashSet<Funcionalidad>();
			funcionalidades2.add(new Funcionalidad(null, "func3", "func-desc3"));
			funcionalidades2.add(new Funcionalidad(null, "func4", "func-desc4"));
			System.out.println(rolBeanRemote);
//
			rolBeanRemote.test();
			System.out.println("va pasando...");
			System.out.println(rolBeanRemote.existePorId(1L));
			System.out.println(rol);
			Rol rol4 = new Rol(null, "Test", "Test");
			rolBeanRemote.actualizar(rol4);
//			System.out.println(rolBeanRemote.crear(rol));
//			rolBeanRemote.crear(rol2);

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			System.out.println("asdasdasdasdasdasdasd");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("TEST TEST TEST: " + e.getMessage());
			System.out.println("TEST 2 2: " + e.getClass());
			e.printStackTrace();
		}

		System.out.println("test");
		System.out.println(usuarioService);
//		System.out.println(usuarioService.obtener(1L));
	}

}
