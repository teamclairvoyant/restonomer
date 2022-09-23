project = 'restonomer'
copyright = '2022, Clairvoyant'
author = 'Clairvoyant'
release = '1.0'

extensions = ['myst_parser', 'sphinx_rtd_theme']

templates_path = ['_templates']
exclude_patterns = []

html_theme = 'sphinx_rtd_theme'
html_static_path = ['_static']

from recommonmark.parser import CommonMarkParser
from recommonmark.transform import AutoStructify

source_suffix = {
    '.rst': 'restructuredtext',
    '.txt': 'restructuredtext',
    '.md': 'markdown',
}

master_doc = 'index'

pygments_style = 'sphinx'

html_sidebars = {
    '**': [
        'about.html',
        'navigation.html',
        'relations.html',  # needs 'show_related': True theme option to display
        'searchbox.html',
        'donate.html',
    ]
}

highlight_language = 'scala'