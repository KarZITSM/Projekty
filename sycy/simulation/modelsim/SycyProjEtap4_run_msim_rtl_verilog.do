transcript on
if {[file exists rtl_work]} {
	vdel -lib rtl_work -all
}
vlib rtl_work
vmap work rtl_work

vlog -vlog01compat -work work +incdir+C:/Users/Admin/Desktop/SZKOLA/studia/SEM2/SYCY/sycyproj_lsfr_do_poprawy/sycy {C:/Users/Admin/Desktop/SZKOLA/studia/SEM2/SYCY/sycyproj_lsfr_do_poprawy/sycy/powermod.v}
vlog -vlog01compat -work work +incdir+C:/Users/Admin/Desktop/SZKOLA/studia/SEM2/SYCY/sycyproj_lsfr_do_poprawy/sycy {C:/Users/Admin/Desktop/SZKOLA/studia/SEM2/SYCY/sycyproj_lsfr_do_poprawy/sycy/random_generator.v}
vlog -vlog01compat -work work +incdir+C:/Users/Admin/Desktop/SZKOLA/studia/SEM2/SYCY/sycyproj_lsfr_do_poprawy/sycy {C:/Users/Admin/Desktop/SZKOLA/studia/SEM2/SYCY/sycyproj_lsfr_do_poprawy/sycy/random_generator2.v}
vlog -vlog01compat -work work +incdir+C:/Users/Admin/Desktop/SZKOLA/studia/SEM2/SYCY/sycyproj_lsfr_do_poprawy/sycy {C:/Users/Admin/Desktop/SZKOLA/studia/SEM2/SYCY/sycyproj_lsfr_do_poprawy/sycy/primenumber.v}
vlog -vlog01compat -work work +incdir+C:/Users/Admin/Desktop/SZKOLA/studia/SEM2/SYCY/sycyproj_lsfr_do_poprawy/sycy {C:/Users/Admin/Desktop/SZKOLA/studia/SEM2/SYCY/sycyproj_lsfr_do_poprawy/sycy/codeMessage.v}
vlog -vlog01compat -work work +incdir+C:/Users/Admin/Desktop/SZKOLA/studia/SEM2/SYCY/sycyproj_lsfr_do_poprawy/sycy {C:/Users/Admin/Desktop/SZKOLA/studia/SEM2/SYCY/sycyproj_lsfr_do_poprawy/sycy/cc.v}
vlog -vlog01compat -work work +incdir+C:/Users/Admin/Desktop/SZKOLA/studia/SEM2/SYCY/sycyproj_lsfr_do_poprawy/sycy {C:/Users/Admin/Desktop/SZKOLA/studia/SEM2/SYCY/sycyproj_lsfr_do_poprawy/sycy/drone.v}
vlog -vlog01compat -work work +incdir+C:/Users/Admin/Desktop/SZKOLA/studia/SEM2/SYCY/sycyproj_lsfr_do_poprawy/sycy {C:/Users/Admin/Desktop/SZKOLA/studia/SEM2/SYCY/sycyproj_lsfr_do_poprawy/sycy/world.v}
vlog -vlog01compat -work work +incdir+C:/Users/Admin/Desktop/SZKOLA/studia/SEM2/SYCY/sycyproj_lsfr_do_poprawy/sycy {C:/Users/Admin/Desktop/SZKOLA/studia/SEM2/SYCY/sycyproj_lsfr_do_poprawy/sycy/random_generator1.v}

