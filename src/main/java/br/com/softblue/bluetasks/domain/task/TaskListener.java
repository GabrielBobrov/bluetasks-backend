package br.com.softblue.bluetasks.domain.task;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PrePersist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.softblue.bluetasks.domain.user.AppUser;
import br.com.softblue.bluetasks.domain.user.AppUserRepository;

@Component
public class TaskListener {
	
	private static AppUserRepository appUserRepository;
	
	@PrePersist//faz o spring chamar esse metodo quando a task estiver pronta para ser salva
	public void onPrePersistHandler(Task task) {
		if(task.getAppUser() == null) {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			AppUser appUser = appUserRepository.findByUsername(username);
			
			if(appUser == null) {
				throw new EntityNotFoundException("O usu�rio "+ username + " n�o foi encontrado");
			}
			task.setAppUser(appUser);
		}
	}
	
	@Autowired
	public void init(AppUserRepository appUserRepository) {
		TaskListener.appUserRepository = appUserRepository;
	}

}
