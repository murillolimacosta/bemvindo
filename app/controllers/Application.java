package controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import models.Institution;
import models.User;
import play.data.validation.Valid;
import play.mvc.Controller;
import util.UserInstitutionParameter;
import util.Utils;
import enumeration.GenderEnum;

public class Application extends Controller {

	public static void index() {
		render();
	}

	public static void index2() {
		render();
	}

	public static void template1() {
		render();
	}

	public static void newAccount() {
		GenderEnum[] genderEnum = GenderEnum.values();
		int i = 0;
		render(genderEnum, i);
	}

	public static void saveNewAccount(@Valid User user, String confirmPassword) {
		if (user.getName() != null) {
			if (!validateForm(user, confirmPassword)) {
				user.password = "";
				confirmPassword = "";
				render("@newAccount", user);
				return;
			} else {
				user.setAdmin(true);
				user.setActive(true);
				user.setPostedAt(new Date());
				user.setInstitutionId(0);
				user.save();
				flash.clear();
				validation.errors().clear();
				flash.success("Cadastro realizado com sucesso! Você está quase lá, " + user.getName() + "! Para entrar, preencha os campos abaixo. :)", "");
				redirect("/login");
			}
		}
		render("@newAccount");
	}

	public static void saveNewInstitution(@Valid Institution institution) {
		UserInstitutionParameter userInstitutionParameter = Admin.getLoggedUserInstitution();
		if (institution.getInstitution() != null) {
			if (!validateForm(institution)) {
				/* The user needs to create a institution */
				User user = userInstitutionParameter.getUser();
				render("@admin.firstStep", institution, user);
				return;
			} else {
				// Links the user to institution
				institution.setUserId(userInstitutionParameter.getUser().getId());
				institution.setPublishedId(userInstitutionParameter.getUser().getId());
				institution.setPostedAt(new Date());
				// Grants one month free to user
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.MONTH, 1);
				institution.setLicenseDate(calendar.getTime());
				institution.save();
				// Links the institution to user
				userInstitutionParameter.getUser().setInstitutionId(institution.getId());
				userInstitutionParameter.getUser().save();
				flash.clear();
				validation.errors().clear();
				flash.success("Instituição '" + institution.getInstitution() + "' criada com sucesso. Aproveite!", "");
				Admin.enableUserConditions(userInstitutionParameter.getUser());
				Admin.index();
			}
		}
		render("@admin.firstStep");
	}

	public static void saveChildUser(@Valid User user, String confirmPassword) {
		if (user.getName() != null) {
			if (!validateForm(user, confirmPassword)) {
				user.password = "";
				confirmPassword = "";
				GenderEnum[] genderEnum = GenderEnum.values();
				render("@newAccount", user, genderEnum);
				return;
			} else {
				user.setAdmin(false);
				user.setActive(true);
				user.setInstitutionId(Admin.getLoggedInstitution().getId());
				user.setPostedAt(new Date());
				user.save();
				flash.clear();
				validation.errors().clear();
				flash.success("Usuário criado com sucesso.", "");
			}
		}
		render("@newAccount");
	}

	private static boolean validateForm(User user, String confirmPassword) {
		boolean ret = false;
		validation.required(user.getName()).message("Favor, insira o seu nome.").key("user.name");
		validation.required(user.getLastName()).message("Favor, insira o seu sobrenome.").key("user.lastName");
		validation.required(user.getEmail()).message("Favor, insira o seu e-mail.").key("user.email");
		validation.email(user.getEmail()).message("Favor, insira o seu e-mail no formato nome@provedor.com.br.").key("user.email");
		validation.isTrue(User.verifyByEmail(user.getEmail()) == null).message("Já existe um usuário com este e-mail.").key("user.email");
		validation.required(user.getPassword()).message("Favor, insira uma senha.").key("user.password");
		validation.required(confirmPassword).message("Favor, digite novamente a senha.").key("confirmPassword");
		validation.isTrue(validatePassword(user.getPassword(), confirmPassword)).message("As senhas digitadas devem ser iguais.").key("confirmPassword");
		params.flash();
		validation.keep();
		if (!validation.hasErrors())
			ret = true;
		else {
			for (play.data.validation.Error error : validation.errors()) {
				System.out.println(error.getKey() + " " + error.message());
			}
		}
		return ret;
	}

	@SuppressWarnings("unused")
	private static boolean validateFormBkp(User user, String confirmPassword) {
		boolean ret = false;
		validation.required(user.getName()).message("Favor, insira o seu nome.").key("user.name");
		validation.required(user.getLastName()).message("Favor, insira o seu sobrenome.").key("user.lastName");
		validation.required(user.getEmail()).message("Favor, insira o seu e-mail.").key("user.email");
		validation.email(user.getEmail()).message("Favor, insira o seu e-mail no formato nome@provedor.com.br.").key("user.email");
		validation.required(user.getBirthdate()).message("Favor, insira sua data de nascimento no formato: 01/01/2001.").key("user.birthdate");
		validation.isTrue(User.verifyByEmail(user.getEmail()) == null).message("Já existe um usuário com este e-mail.").key("user.email");
		validation.required(user.getCpf()).message("Favor, insira o seu CPF.").key("user.cpf");
		validation.isTrue(Utils.validateCPFOrCNPJ(user.getCpf())).message("CPF inválido.").key("user.cpf");
		validation.isTrue(User.verifyByCPF(user.getCpf()) == null).message("Já existe um usuário com este CPF.").key("user.cpf");
		validation.required(user.getAddress()).message("Favor, digite o endereço.").key("user.address");
		validation.required(user.getNeighborhood()).message("Favor, informe o bairro.").key("user.neighborhood");
		validation.required(user.getCep()).message("Favor, informe o CEP.").key("user.cep");
		validation.required(user.getPhone1()).message("Favor, informe o número do seu celular.").key("user.phone1");
		validation.required(user.getPassword()).message("Favor, insira uma senha.").key("user.password");
		validation.required(confirmPassword).message("Favor, digite novamente a senha.").key("confirmPassword");
		validation.isTrue(validatePassword(user.getPassword(), confirmPassword)).message("As senhas digitadas devem ser iguais.").key("confirmPassword");
		params.flash();
		validation.keep();
		if (!validation.hasErrors())
			ret = true;
		else {
			for (play.data.validation.Error error : validation.errors()) {
				System.out.println(error.getKey() + " " + error.message());
			}
		}
		return ret;
	}

	private static boolean validateForm(Institution institution) {
		boolean ret = false;
		validation.required(institution.getInstitution()).message("Favor, insira o nome da Instituição.").key("institution.institution");
		validation.required(institution.getSlogan()).message("Favor, insira o slogan.").key("institution.slogan");
		validation.required(institution.getEmail()).message("Favor, insira o e-mail.").key("institution.email");
		validation.email(institution.getEmail()).message("Favor, insira o e-mail no formato nome@provedor.com.br.").key("institution.email");
		validation.required(institution.getLogo()).message("Favor, insira a logomarca.").key("institution.logo");
		validation.isTrue(Institution.verifyByEmail(institution.getEmail()) == null).message("Já existe uma instituição com este e-mail.").key("institution.email");
		validation.required(institution.getCnpj()).message("Favor, insira o CNPJ.").key("institution.cnpj");
		validation.isTrue(Utils.validateCPFOrCNPJ(institution.getCnpj())).message("CNPJ inválido.").key("institution.cnpj");
		validation.isTrue(Institution.verifyByCnpj(institution.getCnpj()) == null).message("Já existe uma Instituição com este CNPJ.").key("institution.cnpj");
		validation.required(institution.getAddress()).message("Favor, digite o endereço.").key("institution.address");
		validation.required(institution.getNeighborhood()).message("Favor, informe o bairro.").key("institution.neighborhood");
		validation.required(institution.getCep()).message("Favor, informe o CEP.").key("institution.cep");
		validation.required(institution.getPhone1()).message("Favor, informe o telefone.").key("institution.phone1");
		params.flash();
		validation.keep();
		if (!validation.hasErrors())
			ret = true;
		else {
			for (play.data.validation.Error error : validation.errors()) {
				System.out.println(error.getKey() + " " + error.message());
			}
		}
		return ret;
	}

	private static boolean validatePassword(String password, String confirmationPassword) {
		boolean ret = false;
		if (!Utils.isNullOrEmpty(password) && !Utils.isNullOrEmpty(confirmationPassword)) {
			if (password.equals(confirmationPassword))
				ret = true;
		}
		return ret;
	}
}