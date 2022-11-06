package com.client;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.persistence.NoResultException;

import com.exception.UsuarioNoEncontradoException;
import com.model.Funcionalidad;
import com.model.Usuario;
import com.service.FuncionalidadBeanRemote;
import com.service.IUsuarioService;
import com.service.RolBeanRemote;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;

public class Main {

	@EJB
	private static IUsuarioService usuarioService;
	@EJB
	private static RolBeanRemote rolBeanRemote;
	@EJB
	private static FuncionalidadBeanRemote funcionalidadBeanRemote;
	
	private static Usuario usuario = null;

	
	public static void main(String[] args) {
		try {
			InitialContext initialContext = new InitialContext();
			usuarioService = (IUsuarioService) initialContext.lookup("ejb:/GestionUsuarios/UsuarioService!com.service.IUsuarioService");
			rolBeanRemote = (RolBeanRemote) initialContext.lookup("ejb:/GestionUsuarios/RolBean!com.service.RolBeanRemote");
			funcionalidadBeanRemote = (FuncionalidadBeanRemote) initialContext.lookup("ejb:/GestionUsuarios/FuncionalidadBean!com.service.FuncionalidadBeanRemote");
			
			start();


		} catch (Exception e) {
			System.out.println("TEST TEST TEST: " + e.getMessage());
			e.printStackTrace();
		}

		System.out.println("final metodo main");
	}
	
	public static void start() {
		try {
			if (usuario == null) {
				login();
			}
			
			int accion = listarAcciones();
			
			switch (accion) {
				case 1:
					altaUsuario();
					break;
				case 2:
					modificacionUsuario();
					break;
				case 3:
					bajaUsuario();
					break;
				case 4:
					listadoFuncionalidadesUsuario();
					break;
				case 5:
					logout();
					break;
				case 6:
					start();
					break;
				default:
					System.out.println("Error en acciones.");
					break;
			}
			
		} catch (UsuarioNoEncontradoException e) {
			System.out.println(e.getMessage());
			start();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		start();
	}
	
	public static void altaUsuario() throws Exception {
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Ingrese el nombre: ");
		String nombre = sc.nextLine();
		
		System.out.print("Ingrese el apellido: ");
		String apellido = sc.nextLine();
		
		System.out.print("Ingrese el documento: ");
		String documento = sc.nextLine();
		
		System.out.print("Ingrese la contrasenia: ");
		String contrasenia = sc.nextLine();
		
		System.out.print("Ingrese el mail: ");
		String mail = sc.nextLine();
		
		System.out.println("Ingrese el rol: ");
		Long rol = (long) mostrarRoles();
		
		Usuario usuario = new Usuario(null, nombre, apellido, documento, contrasenia, mail, rolBeanRemote.obtener(rol));
		try {
			usuarioService.crear(usuario);
		} catch (Exception e) {
			System.out.println("Error al crear usuario: " + e.getMessage());
		}
	}
	
	public static int mostrarRoles() {
		System.out.println("1 - Analista");
		System.out.println("2 - Tutor");
		System.out.println("3 - Estudiante");
		Scanner sc = new Scanner(System.in);
		int rol = sc.nextInt();
		if (!Arrays.stream(new int[]{1, 2, 3}).anyMatch(i -> i == rol)) {
			System.out.println("El rol que esta intentando seleccionar no es valido.");
			mostrarRoles();
		}
		
		return rol;
	}

	public static void modificacionUsuario() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Ingrese el documento del usuario a modificar: ");
		String documento = sc.nextLine();
		try {
			Usuario usuario = usuarioService.obtenerPorDocumento(documento);
			
			System.out.print("Ingrese el nombre: ");
			String nombre = sc.nextLine();
			
			System.out.print("Ingrese el apellido: ");
			String apellido = sc.nextLine();
			
			System.out.print("Ingrese el mail: ");
			String mail = sc.nextLine();
			
			System.out.println("Ingrese el rol: ");
			Long rol = (long) mostrarRoles();
			
			usuario.setNombre(nombre);
			usuario.setApellido(apellido);
			usuario.setMail(mail);
			usuario.setRol(rolBeanRemote.obtener(rol));
			
			usuarioService.actualizar(usuario);
			
		} catch (NoResultException e) {
			System.out.println("El usuario que intenta modificar no existe: "+ e.getMessage());
		} catch (Exception e) {
			System.out.println("Error al modificar el usuario: " + e.getMessage());
		}
	}
	
	public static void bajaUsuario() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Ingrese el documento del usuario a eliminar: ");
		String documento = sc.nextLine();
		try {
			usuarioService.borrar(documento);
		} catch (Exception e) {
			System.out.println("Error al eliminar usuario: " + e.getMessage());
		}
	}
	
	public static void listadoFuncionalidadesUsuario() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Ingrese el documento del usuario a listar funcionalidades: ");
		String documento = sc.nextLine();
		try {
			Set<Funcionalidad> funcionalidades = usuarioService.obtenerFuncionalidadesUsuarioPorDocumento(documento);
			System.out.println("Funcionalidades: ");
			funcionalidades.forEach(funcionalidad -> {
				System.out.println("Funcionalidad: " + funcionalidad);
			});
		} catch (NoResultException e) {
			System.out.println("El usuario que intenta listar funcionalidades no existe: "+ e.getMessage());
		}
	}

	public static void logout() {
		usuario = null;
		System.out.println("Logout exitoso!");
	}
	
	public static int listarAcciones() {
		System.out.println("Listado de acciones ejecutables:");
		System.out.println("1 - Alta de usuario");
		System.out.println("2 - Modificacion de usuario");
		System.out.println("3 - Baja de usuario");
		System.out.println("4 - Listado de funcionalidades por usuario");
		System.out.println("5 - Logout");
		System.out.println("6 - Listar acciones");
		Scanner sc = new Scanner(System.in);
		int accion = sc.nextInt();
		if (!Arrays.stream(new int[]{1, 2, 3, 4, 5, 6}).anyMatch(i -> i == accion)) {
			System.out.println("La accion que esta intentando ejecutar no es valida.");
			listarAcciones();
		}
		
		return accion;
	}
	
	public static void login() throws UsuarioNoEncontradoException, Exception {
		Scanner sc = new Scanner(System.in);
		System.out.println("Bienvenido, por favor, ingrese su documento y contrasenia para iniciar sesion.");
		System.out.print("Documento: ");
		String documento = sc.nextLine();
		System.out.print("Contrasenia: ");
		String contrasenia = sc.nextLine();
		usuario = usuarioService.login(documento, contrasenia);
	}
}
