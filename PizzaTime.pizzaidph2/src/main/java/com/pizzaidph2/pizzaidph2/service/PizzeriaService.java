package com.pizzaidph2.pizzaidph2.service;

import com.pizzaidph2.pizzaidph2.Component.GenericResponse;
import com.pizzaidph2.pizzaidph2.model.Account;
import com.pizzaidph2.pizzaidph2.model.Pizzeria;
import com.pizzaidph2.pizzaidph2.repository.HybernateAccountRepositoryImpl2;
import com.pizzaidph2.pizzaidph2.repository.HybernatePizzeriaRepositoryImpl2;
import com.pizzaidph2.pizzaidph2.service.amqp.ISagaNotifyService;
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

    @Autowired
    private ISagaNotifyService sagaNotifyService;

    public GenericResponse createPizzeriaService(long userID, Pizzeria obj){

        GenericResponse resp = new GenericResponse();
        Optional<Account> optManager = repoAccount.findById(userID);

        if(optManager.isPresent() && optManager.get().getRegistered() && (optManager.get().getVendor()==null || !optManager.get().getVendor())) {

            Account manager = optManager.get();

            if (obj != null && obj.getName() != null && !obj.getName().isEmpty() && !obj.getName().isBlank()
                    && obj.getVatNumber() != null && !obj.getVatNumber().isEmpty() && !obj.getVatNumber().isBlank()
                    && obj.getAddress() != null && !obj.getAddress().isEmpty() && !obj.getAddress().isBlank()) {
                Pizzeria target = new Pizzeria();
                target.setName(obj.getName());
                target.setVatNumber(obj.getVatNumber().trim());
                target.setAddress(obj.getAddress().trim());
                target.setManagerId(userID);

                manager.setVendor(true);

                target = repo.save(target);
                repoAccount.save(manager);
                System.out.println("Now the user "+manager.getId()+" is the manager of pizzeria "+target.getId());

                System.out.println("I'm calling another BE for menu propagation");
                sagaNotifyService.notifyPizzeriaRegistration(target.getId());

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

    public void createPizzeriaDemo(){
        Pizzeria target = new Pizzeria();

        if(!repo.findByManagerId(1).isPresent()){
            System.out.println("CREATE A NEW PIZZERIA FOR MANAGER "+1);
            target = new Pizzeria();
            target.setName("Pizza Sì");
            target.setVatNumber("10000000");
            target.setAddress("Via della pizzeria 1");
            this.createPizzeriaService(1,target);
        }


        if(!repo.findByManagerId(2).isPresent()){
            System.out.println("CREATE A NEW PIZZERIA FOR MANAGER "+2);
            target = new Pizzeria();
            target.setName("Veggi-zza");
            target.setVatNumber("20000000");
            target.setAddress("Via della pizzeria 2");
            this.createPizzeriaService(2,target);
        }

        if(!repo.findByManagerId(3).isPresent()){
            System.out.println("CREATE A NEW PIZZERIA FOR MANAGER "+3);
            target = new Pizzeria();
            target.setName("Metal Pizzeria");
            target.setVatNumber("30000000");
            target.setAddress("Via della pizzeria 3");
            this.createPizzeriaService(3,target);
        }

        if(!repo.findByManagerId(4).isPresent()){
            System.out.println("CREATE A NEW PIZZERIA FOR MANAGER "+4);
            target = new Pizzeria();
            target.setName("Another Common Pizzeria");
            target.setVatNumber("40000000");
            target.setAddress("Via della pizzeria 4");
            this.createPizzeriaService(4,target);
        }

    }

}
