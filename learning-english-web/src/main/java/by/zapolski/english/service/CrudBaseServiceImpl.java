package by.zapolski.english.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public class CrudBaseServiceImpl<T, ID> implements CrudBaseService<T, ID> {

    @Autowired
    private JpaRepository<T, ID> jpaRepository;

    public List<T> getAll() {
        return jpaRepository.findAll();
    }

    public T getById(ID id) {
        return jpaRepository.getOne(id);
    }

    public void delete(T t) {
        jpaRepository.delete(t);
    }

    public void deleteById(ID id) {
        jpaRepository.deleteById(id);
    }

    public T save(T t) {
        return jpaRepository.save(t);
    }

}
