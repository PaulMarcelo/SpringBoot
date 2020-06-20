package ec.com.paul.springboot.app.item.models.service;

import java.util.List;

import ec.com.paul.springboot.app.item.models.Item;

public interface IItemService {
	public List<Item> findAll();

	public Item findById(Long id, Integer Cantidad);
}
