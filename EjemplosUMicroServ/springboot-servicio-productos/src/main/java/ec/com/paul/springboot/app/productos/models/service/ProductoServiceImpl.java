package ec.com.paul.springboot.app.productos.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ec.com.paul.springboot.app.productos.models.dao.IProductoDao;
import ec.com.paul.springboot.app.productos.models.entity.Producto;

@Service
public class ProductoServiceImpl implements IProductoService {

	@Autowired
	private IProductoDao productoDao;

	@Transactional(readOnly = true)
	public List<Producto> findAll() {
		return (List<Producto>) productoDao.findAll();
	}

	public Producto findById(Long id) {
		return productoDao.findById(id).orElse(null);
	}

}
