package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelos.ClienteSelecionador;
import com.autobots.automanager.modelos.EnderecoAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;



@RestController
@RequestMapping("/Endereco")
public class EnderecoControle {
	@Autowired
	private ClienteRepositorio repoCli;
	@Autowired
	private EnderecoRepositorio repo;
	
	@PutMapping("/EditaEndereço/{cliente}")
	public ResponseEntity<Endereco> editarEnd (
			@PathVariable Long cliente,
			@RequestBody Endereco endereco){
		Cliente find = repoCli.getById(cliente);
		List<Cliente> allCli = repoCli.findAll();
		ClienteSelecionador selecionasCliente = new ClienteSelecionador();
		Cliente select = selecionasCliente.selecionar(allCli,cliente);
		HttpStatus status = HttpStatus.I_AM_A_TEAPOT;
		if (select == null) {
			status = HttpStatus.NOT_FOUND;
		} else {
		EnderecoAtualizador selecionas = new EnderecoAtualizador();
		selecionas.atualizar(find.getEndereco(),endereco);
		repoCli.save(find);
		status = HttpStatus.ACCEPTED;
		}
		return new ResponseEntity<>(status);
	}
}


