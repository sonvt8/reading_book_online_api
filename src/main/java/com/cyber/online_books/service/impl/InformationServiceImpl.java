package com.cyber.online_books.service.impl;

import com.cyber.online_books.entity.Information;
import com.cyber.online_books.repository.InformationRepository;
import com.cyber.online_books.service.InformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InformationServiceImpl implements InformationService {

    private final InformationRepository informationRepository;

    @Autowired
    public InformationServiceImpl(InformationRepository informationRepository) {
        this.informationRepository = informationRepository;
    }

    /**
     * Lấy Thông Tin Web
     *
     * @return Information- Nếu tồn tại Information/ null - nếu không tồn tại Information
     */
    @Override
    public Information getWebInfomation() {
        Optional<Information> information = informationRepository.findFirstByOrderByIdDesc();
        return information.orElse(null);
    }
}
