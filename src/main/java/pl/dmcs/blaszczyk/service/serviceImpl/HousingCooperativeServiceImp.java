package pl.dmcs.blaszczyk.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.dmcs.blaszczyk.model.Entity.HousingCooperative;
import pl.dmcs.blaszczyk.model.Exception.ResourceNotFoundException;
import pl.dmcs.blaszczyk.repository.HousingCooperativeRepository;
import pl.dmcs.blaszczyk.service.HousingCooperativeService;

@Service
public class HousingCooperativeServiceImp implements HousingCooperativeService {

    @Autowired
    HousingCooperativeRepository housingCooperativeRepository;

    @Override
    public HousingCooperative getHousingCooperative(Long id) {
       return housingCooperativeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException());
    }
}
