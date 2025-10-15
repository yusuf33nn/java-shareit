package ru.practicum.shareit.item.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findAllByOwnerIdOrderById(Long ownerId, Pageable pageable);


    Page<Item> searchItemsByAvailableIsTrueAndNameIgnoreCaseOrDescriptionIgnoreCaseOrderById(String name,
                                                                                            String description,
                                                                                            Pageable pageable);
}
