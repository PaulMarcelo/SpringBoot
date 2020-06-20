package ec.com.paul.springboot.app.productos.models.dao;

import org.springframework.data.repository.CrudRepository;

import ec.com.paul.springboot.app.productos.models.entity.Producto;

public interface IProductoDao extends CrudRepository<Producto, Long> {

}
