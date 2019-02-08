package fr.eccad.eccad2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "organization")
// nom de la table
public class Organization {

	// Chaque propriété du modèle correspond à une colonne de la table

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_organization")
	private int idOrganization;

	// toutes les colonnes de la table
	@Column(name = "adress_organization")
	private String adressOrganization;

	@Column(name = "city_organization")
	private String cityOrganization;

	@Column(name = "confirmed")
	private boolean confirmed;

	@Column(name = "fax_oganization")
	private String faxOrganization;

	@Column(name = "full_name_organization")
	private String fullNameOrganization;

	@Column(name = "latitude_organization")
	private float latitudeOrganization;

	@Column(name = "longitude_organization")
	private float longitudeOrganization;

	@Column(name = "phone_organization")
	private String phoneOrganization;

	@Column(name = "short_name_organization")
	private String shortNameOrganization;

	@Column(name = "state_organization")
	private String stateOrganization;

	@Column(name = "url_organization")
	private String urlOrganization;

	@Column(name = "zip_organization")
	private String zipOrganization;

	@Column(name = "id_coutry")
	private int idCOuntry;

	public Organization() {
	}

	public Organization(int idOrganization, String adressOrganization, String cityOrganization, boolean confirmed,
			String faxOrganization, String fullNameOrganization, float latitudeOrganization,
			float longitudeOrganization, String phoneOrganization, String shortNameOrganization,
			String stateOrganization, String urlOrganization, String zipOrganization, int idCOuntry) {
		this.idOrganization = idOrganization;
		this.adressOrganization = adressOrganization;
		this.cityOrganization = cityOrganization;
		this.confirmed = confirmed;
		this.faxOrganization = faxOrganization;
		this.fullNameOrganization = fullNameOrganization;
		this.latitudeOrganization = latitudeOrganization;
		this.longitudeOrganization = longitudeOrganization;
		this.phoneOrganization = phoneOrganization;
		this.shortNameOrganization = shortNameOrganization;
		this.stateOrganization = stateOrganization;
		this.urlOrganization = urlOrganization;
		this.zipOrganization = zipOrganization;
		this.idCOuntry = idCOuntry;
	}

	public int getIdOrganization() {
		return idOrganization;
	}

	public void setIdOrganization(int idOrganization) {
		this.idOrganization = idOrganization;
	}

	public String getAdressOrganization() {
		return adressOrganization;
	}

	public void setAdressOrganization(String adressOrganization) {
		this.adressOrganization = adressOrganization;
	}

	public String getCityOrganization() {
		return cityOrganization;
	}

	public void setCityOrganization(String cityOrganization) {
		this.cityOrganization = cityOrganization;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public String getFaxOrganization() {
		return faxOrganization;
	}

	public void setFaxOrganization(String faxOrganization) {
		this.faxOrganization = faxOrganization;
	}

	public String getFullNameOrganization() {
		return fullNameOrganization;
	}

	public void setFullNameOrganization(String fullNameOrganization) {
		this.fullNameOrganization = fullNameOrganization;
	}

	public float getLatitudeOrganization() {
		return latitudeOrganization;
	}

	public void setLatitudeOrganization(float latitudeOrganization) {
		this.latitudeOrganization = latitudeOrganization;
	}

	public float getLongitudeOrganization() {
		return longitudeOrganization;
	}

	public void setLongitudeOrganization(float longitudeOrganization) {
		this.longitudeOrganization = longitudeOrganization;
	}

	public String getPhoneOrganization() {
		return phoneOrganization;
	}

	public void setPhoneOrganization(String phoneOrganization) {
		this.phoneOrganization = phoneOrganization;
	}

	public String getShortNameOrganization() {
		return shortNameOrganization;
	}

	public void setShortNameOrganization(String shortNameOrganization) {
		this.shortNameOrganization = shortNameOrganization;
	}

	public String getStateOrganization() {
		return stateOrganization;
	}

	public void setStateOrganization(String stateOrganization) {
		this.stateOrganization = stateOrganization;
	}

	public String getUrlOrganization() {
		return urlOrganization;
	}

	public void setUrlOrganization(String urlOrganization) {
		this.urlOrganization = urlOrganization;
	}

	public String getZipOrganization() {
		return zipOrganization;
	}

	public void setZipOrganization(String zipOrganization) {
		this.zipOrganization = zipOrganization;
	}

	public int getIdCOuntry() {
		return idCOuntry;
	}

	public void setIdCOuntry(int idCOuntry) {
		this.idCOuntry = idCOuntry;
	}

	/*
	 * @Override public String toString() { return "Organization{" +
	 * "organization = " + fullNameOrganization + "" + shortNameOrganization + "" +
	 * "Lat. " + latitudeOrganization + "" + "Lon. " + longitudeOrganization + "" +
	 * adressOrganization + urlOrganization; }
	 */

}
