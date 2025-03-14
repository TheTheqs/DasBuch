package com.example.base_server.service;

import com.example.base_server.model.KeyWord;
import com.example.base_server.repository.KeyWordRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KeyWordService {

    @Autowired
    private KeyWordRepository keyWordRepository;

    @Transactional
    //1- Save new keyword or get an existing one.
    public KeyWord saveKeyWord(String value){
        Optional<KeyWord> optionalKeyWord = Optional.ofNullable(keyWordRepository.findByValue(value));
        return optionalKeyWord.orElseGet(() -> keyWordRepository.save(new KeyWord(value)));
    }
    //2- Get all keywords as string list.
    public List<String> getAllKeywordValues() {
        return keyWordRepository.findAllKeywordValues();
    }
}
