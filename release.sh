old_apk="app-release.apk"
new_apk="oggysocial.apk"
release_dir="app/build/outputs/apk/release/"

old_apk_path=$release_dir+$old_apk
mv $old_apk_path $new_apk
