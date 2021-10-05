package am.aca.generactive.service;

import am.aca.generactive.model.Item;
import am.aca.generactive.service.dto.ItemDTO;

import java.util.List;
import java.util.Optional;

public class ItemServiceImpl implements ItemService{
    @Override
    public ItemDTO create(ItemDTO item) {
        return null;
    }

    @Override
    public Item update(Item item) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public Optional<ItemDTO> getItem(Long id) {
        return Optional.empty();
    }

    @Override
    public List<? extends ItemDTO> getAll() {
        return null;
    }

    @Override
    public List<? extends ItemDTO> find(String name) {
        return null;
    }
}
