package com.autobots.automanager.controles;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelos.ClienteSelecionador;
import com.autobots.automanager.modelos.TelefoneAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@RestController
@RequestMapping("/Telefone")
public class TelefoneControle {
	@Autowired
	private TelefoneRepositorio repo;
	@Autowired
	private ClienteRepositorio repoCli;
	@GetMapping("/PegarTodosTelefones")
	public ResponseEntity<List<Telefone>> obterTelefone() {
		List<Telefone> ListTelefone = repo.findAll();
		if (ListTelefone.isEmpty()) {
			ResponseEntity<List<Telefone>> Resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return Resposta;
		}else {
			ResponseEntity<List<Telefone>> Resposta = new ResponseEntity<>(ListTelefone,HttpStatus.FOUND);
			return Resposta;
		}
			
	}
	@PutMapping("/CadastroTelefone/{cliente}")
	public ResponseEntity<Telefone> CadastroTelefone(
			@PathVariable Long cliente,
			@RequestBody Cliente Cadastro){
		List<Cliente> allCli = repoCli.findAll();
		Cliente find = repoCli.getById(cliente);
		ClienteSelecionador selecionas = new ClienteSelecionador();
		Cliente select =  selecionas.selecionar(allCli, cliente);
		HttpStatus status = HttpStatus.CONFLICT;
		if (select == null) {
			status = HttpStatus.I_AM_A_TEAPOT;
		}else {
			find.getTelefones().addAll(Cadastro.getTelefones());
			repoCli.save(find);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}
	
	@PutMapping("/EditaTelefone/{idCli}/{idTel}")
	public ResponseEntity<Telefone> EditaTel(
			@PathVariable Long idCli,
			@PathVariable Long idTel,
			@RequestBody Telefone Body
			){
		Cliente find = repoCli.getById(idCli);
		List<Cliente> allCli = repoCli.findAll(); 
		ClienteSelecionador selecionas = new ClienteSelecionador();
		Cliente select = selecionas.selecionar(allCli, idCli);
		HttpStatus status = HttpStatus.CONFLICT;
		if (select == null) {
			status = HttpStatus.NOT_FOUND;
		} else {
			for (Telefone cliTel: find.getTelefones()) {
				if (cliTel.getId() == idTel) {
					Telefone findTel = repo.getById(cliTel.getId());
					TelefoneAtualizador atualizador = new TelefoneAtualizador();
					atualizador.atualizar(findTel, Body);
					repo.save(findTel);
					status = HttpStatus.ACCEPTED;
				}
			}
		}
		return new ResponseEntity<>(status);
	}
	@DeleteMapping("/DeletaTelefone/{idCli}/{idTel}")
	public ResponseEntity<Telefone> DeletaTel(
			@PathVariable Long idCli,
			@PathVariable Long idTel
			){
		Cliente find = repoCli.getById(idCli);
		List<Cliente> allCli = repoCli.findAll(); 
		ClienteSelecionador selecionas = new ClienteSelecionador();
		Cliente select = selecionas.selecionar(allCli, idCli);
		HttpStatus status = HttpStatus.CONFLICT;
		if (select == null) {
			status = HttpStatus.NOT_FOUND;
		} else {
			for (Telefone cliTel: find.getTelefones()) {
				if (cliTel.getId()== idTel) {
					Telefone findTel = repo.getById(cliTel.getId());
					find.getTelefones().remove(findTel);
					repoCli.save(find);
					status = HttpStatus.ACCEPTED;
				} else {
					status = HttpStatus.I_AM_A_TEAPOT;
				}
			}
		}
		return new ResponseEntity<>(status);
	}
}
