import os
from setuptools import setup

with open(os.path.join(os.getcwd(), 'README.md'), encoding='utf-8') as f:
    long_description = f.read()

setup(
    name = "wfom",
    version = "0.0.2",
    author = "Ryan Byrne",
    author_email = "ryanbyrne142@gmail.com",
    keywords = "wide field optical mapping",
    url = "https://github.com/ryan-byrne/wfom",
    include_package_data=True,
    py_modules=["wfom"],
    long_description=long_description,
    long_description_content_type='text/markdown',
    python_requires='>=3.6',
    license="MIT",
    classifiers=[
        "Programming Language :: Python :: 3",
        "License :: OSI Approved :: MIT License",
        "Operating System :: Microsoft :: Windows",
        "Development Status :: 4 - Beta"
    ],
    project_urls={
        "Bug Reports":"https://github.com/ryan-byrne/wfom/issues",
        "Hillman Lab":"http://orion.bme.columbia.edu/~hillman/"
    },
)
