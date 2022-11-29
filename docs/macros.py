import re
from typing import List


def define_env(env):
    @env.macro
    def javadoc(clazz: str = None, target: str = None) -> str:
        if not "javadoc_url" in env.variables:
            return clazz

        if not clazz:
            return f"{env.variables['javadoc_url'].rstrip('/')}/index.html"
        else:
            if "javadoc_default_package" in env.variables and not clazz.startswith(env.variables['javadoc_default_package']):
                clazz = f"{env.variables['javadoc_default_package'].rstrip('.')}.{clazz}"
            javadoc_url = env.variables["javadoc_url"].rstrip("/")
            path = clazz.replace(".", "/") + ".html"
            if target:
                path = f"{path}#{target}"
            return f"{javadoc_url}/{path}"

    @env.macro
    def javadoc_link(
        clazz: str = None, target: str = None, strip_package: bool = True, strip_clazz: bool = False, title: str = None
    ) -> str:
        name = clazz or "Javadoc"
        if strip_package:
            if clazz and clazz.rfind(".") > -1:
                name = clazz[clazz.rfind(".") + 1 :]
        if target:
            # _target = re.sub(r"([^(][^,]*?\.)*?([^)]+)", lambda m: m.group(2), target)
            _target = target
            if strip_clazz:
                name = _target
            else:
                name = f"{name}.{_target}"
        if title:
            name = title

        return f"[{name}]({javadoc(clazz, target)})"

    @env.macro
    def javadoc_signature(
        clazz: str = None,
        member: str = None,
        package: str = None,
        params: List[str] = list(),
    ) -> str:
        sig = clazz or ""
        if clazz and package:
            sig = f"{package}.{sig}"
        if member:
            sig = f"{sig}#{member}"

        pparams = ",".join(params)
        sig = f"{sig}({pparams})"

        return sig


# schule/ngb/zm/Zeichenmaschine.html#setCursor(java.awt.Image,int,int)
# schule/ngb/zm/Zeichenmaschine.html#getLayer(java.lang.Class)
# schule/ngb/zm/DrawableLayer.html#add(schule.ngb.zm.Drawable...)
