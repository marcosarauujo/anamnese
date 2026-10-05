package com.marcos.anamnese.infrastructure.repository;

import com.marcos.anamnese.infrastructure.entitys.TranscricaoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranscricaoRepository extends MongoRepository<TranscricaoEntity, String> {

    List<TranscricaoEntity> findByCriancaId(Long criancaId);
}
