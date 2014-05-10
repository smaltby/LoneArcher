@echo off

pushd art\vector art
for %%f in (*.svg) do (
    call inkscape %%f --export-png=../%%~nf.png
)
popd