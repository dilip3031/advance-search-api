package com.qburst.genericsearch.services;

import com.google.gson.Gson;
import com.qburst.genericsearch.entitys.UsersEntity;
import com.qburst.genericsearch.repositorys.UsersRepository;
import jakarta.persistence.criteria.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    public List<UsersEntity> getAllUsers(String requestBody) {
        JSONObject request = new JSONObject(requestBody);

        if (request.keySet().isEmpty()) {
            return usersRepository.findAll();
        } else {
            Specification specification = userSearch(request);
            return usersRepository.findAll(specification);
        }
    }

    public UsersEntity createUsers(UsersEntity requestBody) {
        return usersRepository.save(requestBody);
    }

    public Specification<?> userSearch(JSONObject jsonObject) {

        for (String s : jsonObject.keySet()) {
            JSONObject opp;
            JSONArray oppArray;
            Specification<?> entitySpecification;
            ArrayList<Predicate> predicates;

            switch (s) {
                case "_eq":

                    opp = jsonObject.getJSONObject("_eq");
                    for (String key : opp.keySet()) {
                        if (opp.get(key) instanceof JSONArray) {
                            oppArray = opp.getJSONArray(key);
                            entitySpecification = (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get(key)).value(oppArray.toList());
                            return entitySpecification;
                        }
                        entitySpecification = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(key), opp.get(key));
                        return entitySpecification;
                    }
                    break;
                case "_neq":
                    opp = jsonObject.getJSONObject("_neq");
                    for (String key : opp.keySet()) {
                        if (opp.get(key) instanceof JSONArray) {
                            oppArray = opp.getJSONArray(key);
                            entitySpecification = (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get(key)).value(oppArray.toList()).not();
                            return entitySpecification;
                        }
                        entitySpecification = (root, query, criteriaBuilder) -> criteriaBuilder.notEqual(root.get(key), opp.get(key));
                        return entitySpecification;
                    }
                    break;
                case "_lt":
                    opp = jsonObject.getJSONObject("_lt");
                    for (String key : opp.keySet()) {
                        entitySpecification = (root, query, criteriaBuilder) -> {
                            return criteriaBuilder.lessThan(root.get(key), opp.get(key).toString());
                        };
                        return entitySpecification;
                    }
                    break;
                case "_gt":
                    opp = jsonObject.getJSONObject("_gt");
                    for (String key : opp.keySet()) {
                        entitySpecification = (root, query, criteriaBuilder) -> {
                            return criteriaBuilder.greaterThan(root.get(key), opp.get(key).toString());
                        };
                        return entitySpecification;
                    }
                    break;
                case "_lte":
                    opp = jsonObject.getJSONObject("_lte");
                    for (String key : opp.keySet()) {
                        entitySpecification = (root, query, criteriaBuilder) -> {
                            return criteriaBuilder.lessThanOrEqualTo(root.get(key), opp.get(key).toString());
                        };
                        return entitySpecification;
                    }
                    break;
                case "_gte":
                    opp = jsonObject.getJSONObject("_gte");
                    for (String key : opp.keySet()) {
                        entitySpecification = (root, query, criteriaBuilder) -> {
                            return criteriaBuilder.greaterThanOrEqualTo(root.get(key), opp.get(key).toString());
                        };
                        return entitySpecification;
                    }
                    break;
                case "_begins":
                    opp = jsonObject.getJSONObject("_begins");
                    for (String key : opp.keySet()) {
                        entitySpecification = (root, query, criteriaBuilder) -> {
                            return criteriaBuilder.like(root.get(key), opp.get(key).toString() + "%");
                        };
                        return entitySpecification;
                    }
                    break;
                case "_contains":
                    opp = jsonObject.getJSONObject("_contains");
                    for (String key : opp.keySet()) {
                        entitySpecification = (root, query, criteriaBuilder) -> {
                            return criteriaBuilder.like(root.get(key), "%" + opp.get(key).toString() + "%");
                        };
                        return entitySpecification;
                    }
                    break;
                case "_and":
                    JSONArray and = jsonObject.getJSONArray("_and");
                    predicates = new ArrayList<>();
                    entitySpecification = (root, query, criteriaBuilder) -> {
                        for (Object object : and) {
                            Predicate predicate = getPredicate((JSONObject) object, root, query, criteriaBuilder);
                            predicates.add(predicate);
                        }
                        Predicate[] array = predicates.stream().toArray(Predicate[]::new);
                        return criteriaBuilder.and(array);
                    };
                    return entitySpecification;
                case "_or":
                    JSONArray or = jsonObject.getJSONArray("_or");
                    predicates = new ArrayList<>();
                    entitySpecification = (root, query, criteriaBuilder) -> {

                        for (Object object : or) {
                            Predicate predicate = getPredicate((JSONObject) object, root, query, criteriaBuilder);
                            predicates.add(predicate);
                        }
                        Predicate[] array = predicates.stream().toArray(Predicate[]::new);
                        return criteriaBuilder.or(array);
                    };
                    return entitySpecification;
                default:
                    if (jsonObject.get(s) instanceof JSONArray) {
                        oppArray = jsonObject.getJSONArray(s);
                        entitySpecification = (root, query, criteriaBuilder) -> criteriaBuilder.in(root.get(s)).value(oppArray.toList());
                        return entitySpecification;
                    }
                    entitySpecification = (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(s), jsonObject.get(s));
                    return entitySpecification;
            }
        }
        return null;
    }

    public Predicate getPredicate(JSONObject jsonObject, Root<?> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        for (String s : jsonObject.keySet()) {
            JSONObject opp;
            Predicate entitySpecification;
            ArrayList<Predicate> predicates;
            JSONArray oppArray;
            switch (s) {
                case "_eq":
                    opp = jsonObject.getJSONObject("_eq");
                    for (String key : opp.keySet()) {
                        if (opp.get(key) instanceof JSONArray) {
                            oppArray = opp.getJSONArray(key);
                            entitySpecification = criteriaBuilder.in(root.get(key)).value(oppArray.toList());
                            return entitySpecification;
                        }
                        entitySpecification = criteriaBuilder.equal(root.get(key), opp.get(key));
                        return entitySpecification;
                    }
                    break;
                case "_neq":
                    opp = jsonObject.getJSONObject("_neq");
                    for (String key : opp.keySet()) {
                        entitySpecification = criteriaBuilder.notEqual(root.get(key), opp.get(key));
                        return entitySpecification;
                    }
                    break;
                case "_lt":
                    opp = jsonObject.getJSONObject("_lt");
                    for (String key : opp.keySet()) {
                        return criteriaBuilder.lessThan(root.get(key), opp.get(key).toString());
                    }
                    break;
                case "_gt":
                    opp = jsonObject.getJSONObject("_gt");
                    for (String key : opp.keySet()) {
                        return criteriaBuilder.greaterThan(root.get(key), opp.get(key).toString());
                    }
                    break;
                case "_lte":
                    opp = jsonObject.getJSONObject("_lte");
                    for (String key : opp.keySet()) {
                        return criteriaBuilder.lessThanOrEqualTo(root.get(key), opp.get(key).toString());
                    }
                    break;
                case "_gte":
                    opp = jsonObject.getJSONObject("_gte");
                    for (String key : opp.keySet()) {
                        return criteriaBuilder.greaterThanOrEqualTo(root.get(key), opp.get(key).toString());
                    }
                    break;
                case "_begins":
                    opp = jsonObject.getJSONObject("_begins");
                    for (String key : opp.keySet()) {
                        return criteriaBuilder.like(root.get(key), opp.get(key).toString() + "%");
                    }
                    break;
                case "_contains":
                    opp = jsonObject.getJSONObject("_contains");
                    for (String key : opp.keySet()) {
                        return criteriaBuilder.like(root.get(key), "%" + opp.get(key).toString() + "%");
                    }
                    break;
                case "_and":
                    JSONArray and = jsonObject.getJSONArray("_and");
                    predicates = new ArrayList<>();
                    for (Object object : and) {
                        Predicate predicate = getPredicate((JSONObject) object, root, query, criteriaBuilder);
                        predicates.add(predicate);
                    }
                    Predicate[] array = predicates.stream().toArray(Predicate[]::new);
                    return criteriaBuilder.and(array);
                case "_or":
                    JSONArray or = jsonObject.getJSONArray("_or");
                    predicates = new ArrayList<>();
                    for (Object object : or) {
                        Predicate predicate = getPredicate((JSONObject) object, root, query, criteriaBuilder);
                        predicates.add(predicate);
                    }
                    Predicate[] array1 = predicates.stream().toArray(Predicate[]::new);
                    return criteriaBuilder.or(array1);
                default:
                    if (jsonObject.get(s) instanceof JSONArray) {
                        oppArray = jsonObject.getJSONArray(s);
                        entitySpecification = criteriaBuilder.in(root.get(s)).value(oppArray.toList());
                        return entitySpecification;
                    }
                    entitySpecification = criteriaBuilder.equal(root.get(s), jsonObject.get(s));
                    return entitySpecification;
            }
        }
        return null;
    }



}
