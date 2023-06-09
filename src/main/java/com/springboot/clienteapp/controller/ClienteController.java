package com.springboot.clienteapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.clienteapp.models.entity.Ciudad;
import com.springboot.clienteapp.models.entity.Cliente;
import com.springboot.clienteapp.models.service.ICiudadService;
import com.springboot.clienteapp.models.service.IClienteService;


@Controller
@RequestMapping("/views/clientes")
public class ClienteController {
	@Autowired
	private IClienteService clienteService;
	
	@Autowired
	private ICiudadService ciudadService;
	
	@GetMapping({"/", ""})
	public String listarClientes(Model model) {
		List<Cliente> listadoClientes = clienteService.listarTodos();
		model.addAttribute("titulo", "Lista de Clientes"); 
		model.addAttribute("clientes", listadoClientes);
		return "/views/clientes/listar";
	}
	
	@GetMapping("/create")
	public String crear(Model model) {
		
		Cliente cliente = new Cliente();
		List<Ciudad> listCiudades = ciudadService.listaCiudades();
		
		model.addAttribute("titulo", "Formulario: Nuevo Cliente");
		model.addAttribute("cliente", cliente);
		model.addAttribute("ciudades", listCiudades); 
		
		return "/views/clientes/frmCrear";
	}
	
	
	@PostMapping("/save")
	public String guardar(@ModelAttribute Cliente cliente, BindingResult result,
			Model model, RedirectAttributes attribute) {
		
		List<Ciudad> listCiudades = ciudadService.listaCiudades();

		clienteService.guardar(cliente);
		attribute.addFlashAttribute("success", "Cliente registrado con éxito");
		return "redirect:/views/clientes/";
	}

	
	@GetMapping("/edit/{id}")
	public String editar(@PathVariable("id") Long idCliente, Model model, RedirectAttributes attribute) {
		
		Cliente cliente = null;
		
		if (idCliente > 0) {
			cliente = clienteService.buscarPorId(idCliente);
			if (cliente == null) {
				attribute.addFlashAttribute("error", "El ID del cliente no existe!");
				return "redirect:/views/clientes/";
			}
		}else {
			attribute.addFlashAttribute("error", "El ID del cliente no existe!");
			return "redirect:/views/clientes/";
		}
	
		List<Ciudad> listCiudades = ciudadService.listaCiudades();
		
		model.addAttribute("titulo", "Formulario: Editar Cliente");
		model.addAttribute("cliente", cliente);
		model.addAttribute("ciudades", listCiudades); 
		
		return "/views/clientes/frmCrear";
	}
	
	@GetMapping("/delete/{id}")
	public String eliminar(@PathVariable("id") Long idCliente, RedirectAttributes attribute) {
		
		Cliente cliente = null;
		
		if (idCliente > 0) {
			cliente = clienteService.buscarPorId(idCliente);
			if (cliente == null) {
				attribute.addFlashAttribute("error", "El ID del cliente no existe!");
				return "redirect:/views/clientes/";
			}
		}else {
			attribute.addFlashAttribute("error", "El ID del cliente no existe!");
			return "redirect:/views/clientes/";
		}
		
		clienteService.eliminar(idCliente);
		attribute.addFlashAttribute("warning", "Cliente eliminado");
		
		return "redirect:/views/clientes/";
	}
}
