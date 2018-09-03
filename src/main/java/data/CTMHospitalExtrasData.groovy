package data

/**
 * Created by spattela on 10/10/2016.
 */
class CTMHospitalExtrasData {

    static def getHospitalExtra(String extras) {

        def hospitalExtras
        switch (extras) {
            case 'Private Hospital':
                hospitalExtras = 'PrHospital'
                break
            case 'Heart surgery':
                hospitalExtras = 'Cardiac'
                break
            case 'In-Hospital Rehabilitation':
                hospitalExtras = 'Rehabilitation'
                break
            case 'Non Cosmetic Plastic Surgery':
                hospitalExtras = 'PlasticNonCosmetic'
                break
            case 'Palliative Care':
                hospitalExtras = 'Palliative'
                break
            case 'In-Hospital Psychiatry':
                hospitalExtras = 'Psychiatric'
                break
            case 'Gastric Banding':
                hospitalExtras = 'GastricBanding'
                break
            case 'Birth Related Services':
                hospitalExtras = 'Obsteteric'
                break
            case 'Assisted Reproduction':
                hospitalExtras = 'AssistedReproductive'
                break
            case 'Sterilisation':
                hospitalExtras = 'Sterilisation'
                break
            case 'Joint Replacement':
                hospitalExtras = 'JointReplacement'
                break
            case 'Major Eye Surgery':
                hospitalExtras = 'CataractEyeLens'
                break
            case 'Dialysis':
                hospitalExtras = 'RenalDialysis'
                break
            default:
                hospitalExtras = ''
                break

        }

        hospitalExtras
    }
}
