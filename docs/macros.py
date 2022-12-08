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

            path = list()
            name = list()

            for p in clazz.split('.'):
                if p[0].islower():
                    path.append(p)
                else:
                    name.append(p)

            path = '/'.join(path) + '/' + '.'.join(name) + ".html"
            if target:
                path = f"{path}#{target}"
            return f"{javadoc_url}/{path}"

    @env.macro
    def jd(cl: str = None, t: str = None) -> str:
        return javadoc(cl, t)

    @env.macro
    def javadoc_link(
        clazz: str = None,
        target: str = None,
        strip_package: bool = True,
        strip_clazz: bool = False,
        strip_params: bool = True,
        title: str = None
    ) -> str:
        name = clazz or "Javadoc"
        if strip_package:
            if clazz and clazz.rfind(".") > -1:
                name = clazz[clazz.rfind(".") + 1 :]
        if target:
            # _target = re.sub(r"([^(][^,]*?\.)*?([^)]+)", lambda m: m.group(2), target)
            _target = target

            print(f'{_target.strip()=}')
            if m := re.match(r'^(.+?)\((.*)\)$', _target):
                if strip_params and m.group(2):
                    params = m.group(2).split(',')
                    for i, param in enumerate(params):
                        dot = param.rfind('.')
                        if dot >= 0:
                            params[i] = param[dot+1:].strip()
                    params = ", ".join(params)
                    _target = f'{m.group(1)}({params})'

            if strip_clazz:
                name = _target
            else:
                name = f"{name}.{_target}"
        if title:
            name = title

        return f"[`{name}`]({javadoc(clazz, target)})"

    @env.macro
    def jdl(
        cl: str = None,
        t: str = None,
        p: bool = False,
        c: bool = True,
        title: str = None
    ) -> str:
        return javadoc_link(cl, t, strip_package=not p, strip_clazz=not c, strip_params=True, title=title)

    @env.macro
    def jdc(
        cl: str,
        p: bool = False
    ) -> str:
        return javadoc_link(cl, strip_package=not p)

    @env.macro
    def jdm(
        cl: str,
        t: str,
        p: bool = False,
        c: bool = False
    ) -> str:
        return javadoc_link(cl, t, strip_package=not p, strip_clazz=not c)

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

    @env.macro
    def jds(
        cl: str = None,
        m: str = None,
        pkg: str = None,
        params: List[str] = list(),
    ) -> str:
        javadoc_signature(cl, m, pkg, params)


# schule/ngb/zm/Zeichenmaschine.html#setCursor(java.awt.Image,int,int)
# schule/ngb/zm/Zeichenmaschine.html#getLayer(java.lang.Class)
# schule/ngb/zm/DrawableLayer.html#add(schule.ngb.zm.Drawable...)
