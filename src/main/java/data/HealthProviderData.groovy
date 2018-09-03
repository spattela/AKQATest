package data

/**
 * Created by ssalunke on 25/07/2016.
 */
class HealthProviderData {

    static def getProviderCode(String provider) {

        def providerCode
        switch (provider.toUpperCase()) {
            case 'HIF':
                providerCode = 'HIF'
                break
            case 'MY OWN':
                providerCode = 'MYO'
                break
            case 'HBF':
                providerCode = 'HBF'
                break
            case 'HCF':
                providerCode = 'HCF'
                break
            case 'NAVY HEALTH':
                providerCode = 'NHB'
                break
            case 'FRANK':
                providerCode = 'FRA'
                break
            case 'BUDGET DIRECT':
                providerCode = 'BUD'
                break
            case 'AHM':
                providerCode = 'AHM'
                break
            case 'BUPA':
                providerCode = 'BUP'
                break
            case 'AUSTRALIAN UNITY':
                providerCode = 'AUF'
                break
            case 'CBHS':
                providerCode = 'CBH'
                break
            case 'NIB':
                providerCode = 'NIB'
                break
            case 'CUA':
                providerCode = 'CUA'
                break
            case 'GMHBA':
                providerCode = 'GMH'
                break
            case 'QCHF':
                providerCode = 'QCH'
                break
            case 'TUH':
                providerCode = 'QTU'
                break
            case 'WESTFUND':
                providerCode = 'WFD'
                break
            default:
                providerCode = 'HIF'
                break
        }

        providerCode
    }

}
