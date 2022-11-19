package com.autobots.automanager.controles;

import java.util.List;

import org.apache.catalina.connector.Response;
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
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelos.ClienteSelecionador;
import com.autobots.automanager.modelos.DocumentoAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentosRepositorio;

@RestController
@RequestMapping("/Documentos")
public class Documentos {
	@Autowired
	private DocumentosRepositorio repo;
	@Autowired
	private ClienteRepositorio repoCli;
	@GetMapping("/PegarTodosDocumentos")
	public ResponseEntity<List<Documento>> obterDocumentos() {
		List<Documento> ListDocumento = repo.findAll();
		if (ListDocumento.isEmpty()) {
			ResponseEntity<List<Documento>> Resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return Resposta;
		}else {
			ResponseEntity<List<Documento>> Resposta = new ResponseEntity<>(ListDocumento,HttpStatus.FOUND);
			return Resposta;
		}
			
	}
	@PutMapping("/CadastroDocumentos/{cliente}")
	public ResponseEntity<Documento> CadastroDocumento(
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
			find.getDocumentos().addAll(Cadastro.getDocumentos());
			repoCli.save(find);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}
	
	@PutMapping("/EditaDocumento/{idCli}/{idDoc}")
	public ResponseEntity<Documento> EditaDoc(
			@PathVariable Long idCli,
			@PathVariable Long idDoc,
			@RequestBody Documento Body
			){
		Cliente find = repoCli.getById(idCli);
		List<Cliente> allCli = repoCli.findAll(); 
		ClienteSelecionador selecionas = new ClienteSelecionador();
		Cliente select = selecionas.selecionar(allCli, idCli);
		HttpStatus status = HttpStatus.CONFLICT;
		if (select == null) {
			status = HttpStatus.NOT_FOUND;
		} else {
			for (Documento cliDocs: find.getDocumentos()) {
				if (cliDocs.getId() == idDoc) {
					Documento findDoc = repo.getById(cliDocs.getId());
					DocumentoAtualizador atualizador = new DocumentoAtualizador();
					atualizador.atualizar(findDoc, Body);
					repo.save(findDoc);
					status = HttpStatus.ACCEPTED;
				}
			}
		}
		return new ResponseEntity<>(status);
	}
	@DeleteMapping("/DeletaDocumento/{idCli}/{idDoc}")
	public ResponseEntity<Documento> DeletaDoc(
			@PathVariable Long idCli,
			@PathVariable Long idDoc
			){
		Cliente find = repoCli.getById(idCli);
		List<Cliente> allCli = repoCli.findAll(); 
		ClienteSelecionador selecionas = new ClienteSelecionador();
		Cliente select = selecionas.selecionar(allCli, idCli);
		HttpStatus status = HttpStatus.CONFLICT;
		if (select == null) {
			status = HttpStatus.NOT_FOUND;
		} else {
			for (Documento cliDocs: find.getDocumentos()) {
				if (cliDocs.getId()== idDoc) {
					Documento findDoc = repo.getById(cliDocs.getId());
					find.getDocumentos().remove(findDoc);
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
