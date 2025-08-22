package com.DPRIHKAT.service;

import com.DPRIHKAT.entity.Agent;
import com.DPRIHKAT.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AgentService {

    @Autowired
    private AgentRepository agentRepository;

    public List<Agent> findAll() {
        return agentRepository.findAll();
    }

    public Agent findById(UUID id) {
        return agentRepository.findById(id).orElse(null);
    }

    public Agent save(Agent agent) {
        return agentRepository.save(agent);
    }

    public Agent update(UUID id, Agent agent) {
        if (agentRepository.existsById(id)) {
            agent.setId(id);
            return agentRepository.save(agent);
        }
        return null;
    }

    public void deleteById(UUID id) {
        agentRepository.deleteById(id);
    }
}
