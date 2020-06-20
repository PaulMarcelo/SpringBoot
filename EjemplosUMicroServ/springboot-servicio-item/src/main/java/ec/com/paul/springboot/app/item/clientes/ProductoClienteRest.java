package ec.com.paul.springboot.app.item.clientes;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ec.com.paul.springboot.app.item.models.Producto;

//@FeignClient(name = "servicio-productos", url = "localhost:8001") //sin Ribbon
@FeignClient(name = "servicio-productos")
public interface ProductoClienteRest {

	@GetMapping("/listar")
	public List<Producto> listar();

	@GetMapping("/ver/{id}")
	public Producto detalle(@PathVariable(value = "id") Long id);

}
