package com.pizzaidph2.pizzaidph2.service;

import com.pizzaidph2.pizzaidph2.Component.GenericResponse;
import com.pizzaidph2.pizzaidph2.model.Account;
import com.pizzaidph2.pizzaidph2.model.Pizzeria;
import com.pizzaidph2.pizzaidph2.repository.HybernateAccountRepositoryImpl2;
import com.pizzaidph2.pizzaidph2.repository.HybernatePizzeriaRepositoryImpl2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PizzeriaService {

    @Autowired
    HybernatePizzeriaRepositoryImpl2 repo;

    @Autowired
    HybernateAccountRepositoryImpl2 repoAccount;

    @Autowired
    GeneralService genService;

    public GenericResponse createPizzeriaService(long userID, Pizzeria obj){

        GenericResponse resp = new GenericResponse();
        Optional<Account> optManager = repoAccount.findById(userID);

        if(optManager.isPresent() && optManager.get().getRegistered() && (optManager.get().getVendor()==null || !optManager.get().getVendor())) {

            Account manager = optManager.get();

            if (obj != null && obj.getVatNumber() != null && !obj.getVatNumber().isEmpty() && !obj.getVatNumber().isBlank()
                    && obj.getAddress() != null && !obj.getAddress().isEmpty() && !obj.getAddress().isBlank()) {
                Pizzeria target = new Pizzeria();
                target.setVatNumber(obj.getVatNumber().trim());
                target.setAddress(obj.getAddress().trim());
                target.setManagerId(userID);

                manager.setVendor(true);

                repo.save(target);
                repoAccount.save(manager);
                System.out.println("Now the user "+manager.getId()+" is the manager of pizzeria "+target.getId());

                resp.setStatusCode(GenericResponse.OK_CODE);
                resp.setStatusReason(GenericResponse.OK_MESSAGE);
                return resp;
            } else {
                resp.setStatusCode(GenericResponse.INVALID_PARAMETER_CODE);
                resp.setStatusReason(GenericResponse.INVALID_PARAMETER_MESSAGE);
                return resp;
            }
        }else{
            resp.setStatusCode(GenericResponse.INVALID_PARAMETER_CODE);
            resp.setStatusReason(GenericResponse.INVALID_PARAMETER_MESSAGE);
            return resp;
        }
    }

    //This API is deprecated
    public String openPizzeriaService(long id){
        GenericResponse resp = new GenericResponse();
        Pizzeria target = genService.getInternalPizzeriaInfo(id);
        if(target!=null){
            target.setAvaillability(!target.getAvaillability());
            repo.save(target);

            resp.setStatusCode(GenericResponse.OK_CODE);
            resp.setStatusReason(GenericResponse.OK_MESSAGE);
            return resp.jsonfy();
        }
        resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
        resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
        return resp.jsonfy();
    }


    public String getPizzeriaInfo(long id){
        GenericResponse resp = new GenericResponse();
        Pizzeria target = genService.getInternalPizzeriaInfo(id);
        if(target!=null){
            resp.setStatusCode(GenericResponse.OK_CODE);
            resp.setStatusReason(GenericResponse.OK_MESSAGE);
            resp.setPizzeria(target);
            return resp.jsonfy();
        }
        resp.setStatusCode(GenericResponse.FAILED_AUTHENTICATION_CODE);
        resp.setStatusReason(GenericResponse.FAILED_AUTHENTICATION_MESSAGE);
        return resp.jsonfy();
    }

    public Optional<Pizzeria> getPizzeriaFromManagerId(Long managerId){
        Optional<Pizzeria> optTarget = repo.findByManagerId(managerId);
        if(optTarget.isPresent()){
            System.out.println("I found the following pizzeria: "+optTarget.get().jsonfy());
        }

        return optTarget;
    }


}
