package org.delcom.pam_p4_ifs23036.network.plants.service

interface IPlantAppContainer {
    val plantRepository: IPlantRepository
    val movieRepository: IMovieRepository
}
