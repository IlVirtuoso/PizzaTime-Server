package com.pizzatime.pizzaengine.Service;

import com.pizzatime.pizzaengine.Component.GenericResponse;
import com.pizzatime.pizzaengine.Model.*;
import com.pizzatime.pizzaengine.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class MenuService {

    @Autowired
    GenericService genService;

    @Autowired
    HybernatePizzaRepositoryImpl repoPizza;

    @Autowired
    HybernateIngredientRepositoryImpl repoIngr;

    @Autowired
    HybernateMenuRepositoryImpl repoMenu;

    @Autowired
    HybernateMenuRowIngredientRepositoryImpl repoMenuIngr;

    @Autowired
    HybernateMenuRowPizzaRepositoryImpl repoMenuPizza;

    public String createMenu(long pizzeriaForMenu) {
        GenericResponse resp = new GenericResponse();

        Optional<Menu> opt = repoMenu.findByPizzeriaId(pizzeriaForMenu);

        if(opt.isPresent()){
            resp.setStatusCode(GenericResponse.ALREADY_EXISTING_ITEM_CODE);
            resp.setStatusReason(GenericResponse.ALREADY_EXISTING_ITEM_MESSAGE);
            return resp.jsonfy();
        }else {
            Menu m = new Menu();
            m.setPizzeriaId(pizzeriaForMenu);
            m = repoMenu.save(m);
            resp.setMenu(m);
            resp.setStatusCode(GenericResponse.OK_CODE);
            resp.setStatusReason(GenericResponse.OK_MESSAGE);
            return resp.jsonfy();
        }
    }

    public GenericResponse addPizzaRow(long pizzeriaId, String commonName, long pizzaId, float cost ){
        GenericResponse resp = new GenericResponse();

        MenuRowPizza mrp = new MenuRowPizza();
        mrp.setCommonName(commonName);
        mrp.setCost(cost);

        Optional<Pizza> targetPizza = repoPizza.findById(pizzaId);
        if(targetPizza.isPresent()){
            mrp.setPizza(targetPizza.get());
        }else{
            resp.setStatusCode(GenericResponse.INVALID_PARAMETER_CODE);
            resp.setStatusReason(GenericResponse.INVALID_PARAMETER_MESSAGE);
            return resp;
        }
        return addPizzaRow(pizzeriaId, mrp);
    }

    @Transactional
    public GenericResponse addPizzaRow(long pizzeriaId, MenuRowPizza mrp){
        GenericResponse resp = new GenericResponse();

        System.out.println("time to add a new row");
        Optional<Menu> mTarget = repoMenu.findByPizzeriaId(pizzeriaId);
        if(mTarget.isPresent() && mrp != null){
            System.out.println("I found the menu and check the row");
            if(isPizzaInMenu(mTarget.get(), mrp.getPizza()) == null) {
                System.out.println("CREATE AND ADD THE NEW ROW");
                Pizza p = genService.getOnePizzaPerfectMatch(mrp.getPizza().getSeasonings());
                mrp.setPizza(p);
                MenuRowPizza newMenuRow = repoMenuPizza.save(mrp);
                Menu m = mTarget.get();
                m.getPizzaRows().add(newMenuRow);
                repoMenu.save(m);
                resp.setStatusCode(GenericResponse.OK_CODE);
                resp.setStatusReason(GenericResponse.OK_MESSAGE);
                return resp;
            }else{
                System.out.println("IT IS SUFFICIENT TO MODIFY THE ROW");
                MenuRowPizza rowToEdit = isPizzaInMenu(mTarget.get(), mrp.getPizza());
                Pizza p = genService.getOnePizzaPerfectMatch(mrp.getPizza().getSeasonings());
                rowToEdit.setPizza(p);
                rowToEdit.setCommonName(mrp.getCommonName());
                rowToEdit.setCost(mrp.getCost());
                repoMenuPizza.save(rowToEdit);
                Menu m = mTarget.get();
                repoMenu.save(m);

                System.out.println("Edit an already Existing pizza row");
                resp.setStatusCode(GenericResponse.OK_CODE);
                resp.setStatusReason(GenericResponse.OK_MESSAGE);
                return resp;
            }
        }else{
        resp.setStatusCode(GenericResponse.NOT_EXISTING_ITEM_CODE);
        resp.setStatusReason(GenericResponse.NOT_EXISTING_ITEM_MESSAGE);
        return resp;
        }
    }

    @Transactional
    public GenericResponse addIngredientRow(long pizzeriaId, String commonName, long ingrId, float cost ) {
        GenericResponse resp = new GenericResponse();

        MenuRowIngredient mri = new MenuRowIngredient();
        mri.setCost(cost);

        Optional<Ingredient> targetIngr = repoIngr.findById(ingrId);
        if(targetIngr.isPresent()){

            mri.setCommonName(targetIngr.get().getCommonName());
            mri.setIngredient(targetIngr.get());
        }else{
            resp.setStatusCode(GenericResponse.INVALID_PARAMETER_CODE);
            resp.setStatusReason(GenericResponse.INVALID_PARAMETER_MESSAGE);
            return resp;
        }
        return addIngredientRow(pizzeriaId, mri);
    }

    @Transactional
    public GenericResponse addIngredientRow(long pizzeriaId, MenuRowIngredient mri){
        GenericResponse resp = new GenericResponse();

        Optional<Menu> mTarget = repoMenu.findByPizzeriaId(pizzeriaId);
        if(mTarget.isPresent() && mri != null){
            if(isIngrInMenu(mTarget.get(), mri.getIngredient()) == null) {
                System.out.println("CREATE AND ADD THE NEW ROW");
                MenuRowIngredient newMenuRow = repoMenuIngr.save(mri);
                Menu m = mTarget.get();
                m.getIngrRows().add(newMenuRow);
                repoMenu.save(m);
                resp.setStatusCode(GenericResponse.OK_CODE);
                resp.setStatusReason(GenericResponse.OK_MESSAGE);
                return resp;
            }else{
                System.out.println("IT IS SUFFICIENT TO MODIFY THE ROW");
                MenuRowIngredient rowToEdit = isIngrInMenu(mTarget.get(), mri.getIngredient());
                rowToEdit.setIngredient(mri.getIngredient());
                rowToEdit.setCommonName(mri.getCommonName());
                rowToEdit.setCost(mri.getCost());
                repoMenuIngr.save(mri);
                Menu m = mTarget.get();
                repoMenu.save(m);

                System.out.println("Edit an already existing ingredient row");
                resp.setStatusCode(GenericResponse.OK_CODE);
                resp.setStatusReason(GenericResponse.OK_MESSAGE);
                return resp;
            }
        }
        resp.setStatusCode(GenericResponse.INVALID_PARAMETER_CODE);
        resp.setStatusReason(GenericResponse.INVALID_PARAMETER_MESSAGE);
        return resp;
    }

    @Transactional
    public MenuRowPizza isPizzaInMenu(Menu m, Pizza p){
        Pizza target = genService.getOnePizzaPerfectMatch(p.getSeasonings());
        if(target!=null){
            Set<MenuRowPizza> mp = m.getPizzaRows();
            for(MenuRowPizza checkId : mp){
                if(checkId.getPizza().getId() == target.getId())
                    return checkId;
            }
        }return null;
    }


    public MenuRowIngredient isIngrInMenu(Menu m, Ingredient p){
        Optional<Ingredient> target = repoIngr.findBycommonName(p.getCommonName());
        if(target.isPresent()){
            Set<MenuRowIngredient> mi = m.getIngrRows();
            for(MenuRowIngredient checkId : mi){
                if(checkId.getIngredient().getId() == target.get().getId())
                    return checkId;
            }
        }return null;
    }


}
