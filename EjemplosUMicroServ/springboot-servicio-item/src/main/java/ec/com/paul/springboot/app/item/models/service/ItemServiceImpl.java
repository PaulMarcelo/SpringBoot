package ec.com.paul.springboot.app.item.models.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import ec.com.paul.springboot.app.item.models.Item;
import ec.com.paul.springboot.app.item.models.Producto;

@Service("serviceRestTemplate")
public class ItemServiceImpl implements IItemService {

	@Autowired
	private RestTemplate clienteRest;

	// sin balanceo de carga
	// public List<Item> findAll() {
	// List<Producto> productos = Arrays
	// .asList(clienteRest.getForObject("http://localhost:8001/listar",
	// Producto[].class));
	// return productos.stream().map(t -> new Item(t,
	// 1)).collect(Collectors.toList());
	// }

	@Override
	public List<Item> findAll() {
		List<Producto> productos = Arrays
				.asList(clienteRest.getForObject("http://servicio-productos/listar", Producto[].class));
		return productos.stream().map(p -> new Item(p, 1)).collect(Collectors.toList());
	}

	// public Item findById(Long id, Integer cantidad) {
	// Map<String, String> parametros = new HashMap<>();
	// parametros.put("id", id.toString());
	// Producto producto =
	// clienteRest.getForObject("http://localhost:8001/ver/{id}", Producto.class,
	// parametros);
	// return new Item(producto, cantidad);
	// }

	@Override
	public Item findById(Long id, Integer cantidad) {
		Map<String, String> pathVariables = new HashMap<String, String>();
		pathVariables.put("id", id.toString());
		Producto producto = clienteRest.getForObject("http://servicio-productos/ver/{id}", Producto.class,
				pathVariables);
		return new Item(producto, cantidad);
	}

}
