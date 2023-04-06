package br.com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.entity.Veiculo;
import br.com.response.ResponseRest;
import br.com.response.ResponseRest.messageType;
import br.com.service.VeiculoService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("garagem/check")
public class CheckController {
	
	@Autowired
	VeiculoService serviceCarro;

	@PatchMapping("/entradaSaida/{id}")
	public ResponseEntity<ResponseRest> AlteraCheck(@PathVariable("id") Long id, @RequestBody Veiculo veiculo, ResponseRest response) {
		if(veiculo.getCheckInOut() == null) {
			response.setMessage("O campo referente ao Tipo de conta do checkInOut, deve ser preenchido com true (Check-in) ou false (Check-out)");
	    	response.setType(messageType.ERRO);
	    	return new ResponseEntity<ResponseRest>(response,HttpStatus.BAD_REQUEST);
		}
		alteraCheckInOut(veiculo, veiculo.getCheckInOut(), id);
		if(veiculo.getCheckInOut().equals(true)) {
		response.setMessage("Check-in efetuado com sucesso");
    	response.setType(messageType.SUCESSO);
		} else if(veiculo.getCheckInOut().equals(false)){
		response.setMessage("Check-out efetuado com sucesso");
	    response.setType(messageType.SUCESSO);
    	}
		return new ResponseEntity<ResponseRest>(response,HttpStatus.OK);

	}
	
	@GetMapping("/{checkInOut}")
	@RequestMapping( value = "/verificaCheckin")
	public ResponseEntity<List<Veiculo>> listaCheckInOut(@RequestParam (name = "checkInOut", required = true) Boolean checkIn){
		return ResponseEntity.status(HttpStatus.OK).body(serviceCarro.findByCheck(checkIn));			
	}	
	
	public ResponseEntity<Veiculo> alteraCheckInOut(Veiculo veiculo, Boolean checkInOut, Long id){
		Veiculo veiculoCadastrado = serviceCarro.findById(id);
		veiculo.setId(id);
		veiculo.setAno(veiculoCadastrado.getAno());
		veiculo.setCheckInOut(checkInOut);
		veiculo.setMarca(veiculoCadastrado.getMarca());
		veiculo.setModelo(veiculoCadastrado.getModelo());
		veiculo.setPlaca(veiculoCadastrado.getPlaca());
        return serviceCarro.create(veiculo);
	}	
}
